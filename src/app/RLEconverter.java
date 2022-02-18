package app;

import java.util.Scanner;
import java.io.*;

public class RLEconverter {
   private final static int DEFAULT_LEN = 100; // used to create arrays.

   /*
    *  This method reads in an uncompressed ascii image file that contains 
    *  2 characters. It stores each line of the file in an array.
    *  It then calls compressAllLines to get an array that stores the compressed
    *  version of each uncompressed line from the file. The compressed array
    *  is then passed to the getCompressedFileStr method which returns a String
    *  of all compressed lines (the two charcaters are written in the first line)
    *  in CSV format. This String is written to a text file with the prefix "RLE_"
    *  added to the original, uncompressed file name.
    *  Note that dataSize keeps track of the number of lines in the file. The array 
    *  that holds the lines of the file is initialized to the DEFAULT_LEN, which 
    *  is assumed to be << the number of lines in the file.
    */   
  public void compressFile(String fileName) throws IOException{
    Scanner scan = new Scanner(new FileReader(fileName));
    String line = null;
    String[] decompressed = new String [DEFAULT_LEN];
    int dataSize = 0;
    while(scan.hasNext()){
      line = scan.next();
      if(line != null && line.length()>0)
        decompressed[dataSize]=line;
        dataSize++;
    }
    scan.close();
    char[] fileChars = discoverAsciiChars(decompressed, dataSize); 
    String[] compressed = compressAllLines(decompressed, dataSize, fileChars);
    writeFile(getCompressedFileStr(compressed, fileChars), "RLE_"+fileName);
  }
  
   
/*
 * This method implements the RLE compression algorithm. It takes a line of uncompressed data
 * from an ascii file and returns the RLE encoding of that line in CSV format.
 * The two characters that make up the image file are passed in as a char array, where
 * the first cell contains the first character that occurred in the file.
*/
public String compressLine(String line, char[] fileChars){
   //TODO: Implement this method
   //okay Im on the right track, just need to find a way to take in shit that isn't just one damn number
    StringBuilder compressed= new StringBuilder();
    int[] count = new int[2];
    for (int i = 0; i < line.length(); i+=0){
      for (int j = 0; j < fileChars.length; j++){
        count[j]=0;
        while(i < line.length() && line.charAt(i) == fileChars[j]){
          count[j]++;
          i++;
        }
        compressed.append(count[j]).append(',');
      }
  }
  compressed.deleteCharAt(compressed.length()-1);
  if(compressed.charAt(compressed.length()-1)=='0'){
    compressed.deleteCharAt(compressed.length()-1).deleteCharAt(compressed.length()-1);
  }
  return compressed.toString();
}

  /*
   *  This method discovers the two ascii characters that make up the image. 
   *  It iterates through all of the lines and writes each compressed line
   *  to a String array which is returned. The method compressLine is called on 
   *  each line.
   *  The dataSize is the number of lines in the file, which is likely to be << the length of lines.
   */
  public String[] compressAllLines(String[] lines, int dataSize, char[] fileChars){
      //TODO: Implement this method
      String[] compressedAll = new String[dataSize];
      for(int i = 0; i < dataSize; i++){
       compressedAll[i] = compressLine(lines[i], fileChars);
      }
      return compressedAll;
}

/*
 *  This method assembles the lines of compressed data for
 *  writing to a file. The first line must be the 2 ascii characters
 *  in comma-separated format. 
 */
public String getCompressedFileStr(String[] compressed, char[] fileChars) {
    //TODO: Implement this method
    StringBuilder fileStr = new StringBuilder();
    fileStr.append(fileChars[0]).append(',').append(fileChars[1]);
    fileStr.append('\n');
    for(String n : compressed){
      fileStr.append(n).append('\n');
    }
      return fileStr.toString();
}
   /*
    *  This method reads in an RLE compressed ascii image file that contains 
    *  2 characters. It stores each line of the file in an array.
    *  It then calls decompressAllLines to get an array that stores the decompressed
    *  version of each compressed line from the file. The first row contains the two 
    *  ascii charcaters used in the original image file. The decompressed array
    *  is then passed to the getDecompressedFileStr method which returns a String
    *  of all decompressed lines, thus restoring the original, uncompressed image.
    *  This String is written to a text file with the prefix "DECOMP_"
    *  added to the original, compressed file name.
    *  Note that dataSize keeps track of the number of lines in the file. The array 
    *  that holds the lines of the file is initialized to the DEFAULT_LEN, which 
    *  is assumed to be << the number of lines in the file.
    */   
  public void decompressFile(String fileName) throws IOException{
    Scanner scan = new Scanner(new FileReader(fileName));
    String line = null;
    String[] compressed = new String [DEFAULT_LEN];
    int dataSize =0;
    while(scan.hasNext()){
      line = scan.next();
      if(line != null && line.length()>0)
        compressed[dataSize]=line;
        dataSize++;
    }
    scan.close();
    String[] decompressed = decompressAllLines(compressed, dataSize);
    writeFile(getDecompressedFileStr(decompressed), "DECOMP_"+fileName);
  }
 
   /*
   * This method decodes lines that were encoded by the RLE compression algorithm. 
   * It takes a line of compressed data and returns the decompressed, or original version
   * of that line. The two characters that make up the image file are passed in as a char array, 
   * where the first cell contains the first character that occurred in the file.
   */
   public String decompressLine(String line, char[] fileChars){
      //TODO: Implement this method
      String decomp = "";
      String[] lines = line.split(",");
      int element;
      int k = 2;

      for(int i = 0; i < lines.length; i++){
        element = Integer.parseInt(lines[i]);
        if((k%2!=0)){
          for (int j = 0; j<element; j++){
            decomp += fileChars[1];
          }
          if(i+1<lines.length){
            k++;
            continue;
          }
        }
        if((k%2)==0){
          for(int j = 0; j<element; j++){
            decomp += fileChars[0];
          }
          if(i+1<lines.length){
            k++;
            continue;
          }
        }
      }
   /*   String decompressed="";
      String[] lines = line.split(",");
      int a;
      int b = 2;
      for(int i = 0; i < lines.length; i++){
        a = Integer.parseInt(lines[i]);
        if((b%2) != 0){
          for (int j = 0; j < a; j++){
            decompressed += fileChars[1];
          }
        if(i+1<line.length()){
          b++;
          continue;
        }
        if((b%2)==0){
          for(int j = 0; j<a; j++){
            decompressed += a;
          }
        if(i+1<lines.length){
          b++;
          continue;
        }
        }
        }
        /*
        for(char letters : fileChars){
          
          for(int j = Integer.parseInt(lines[i]); j > 0; j--){
            decompressed += letters;
          }
          i++;
        }
        *
      }
      */
      return decomp;
   }
    /*
   *  This method iterates through all of the compressed lines and writes 
   *  each decompressed line to a String array which is returned. 
   *  The method decompressLine is called on each line. The first line in
   *  the compressed array passed in are the 2 ascii characters used to make
   *  up the image. 
   *  The dataSize is the number of lines in the file, which is likely to be << the length of lines.
   *  The array returned contains only the decompressed lines to be written to the decompressed file.
   */
  public String[] decompressAllLines(String[] lines, int dataSize){
     //TODO: Implement this method
     String[] decompressedAll = new String[dataSize-1];
     char[] fileChars1 = lines[0].toCharArray();
     for(int i = 1; i < dataSize; i++){
      decompressedAll[i-1] += decompressLine(lines[i], fileChars1);
     }
     return decompressedAll;
  }
  
  /*
   *  This method assembles the lines of decompressed data for
   *  writing to a file. 
   */
  public String getDecompressedFileStr(String[] decompressed){
   //TODO: Implement this method
   StringBuilder fileStr1 = new StringBuilder();
   for(String n : decompressed){
     fileStr1.append(n).append('\n');
   }
     return fileStr1.toString();
  }

  // assume the file contains only 2 different ascii characters.
  public char[] discoverAsciiChars(String[] decompressed, int dataSize){
//TODO: Implement this method
  return null;
}



   
   public void writeFile(String data, String fileName) throws IOException{
		PrintWriter pw = new PrintWriter(fileName);
      pw.print(data);
      pw.close();
   }
}