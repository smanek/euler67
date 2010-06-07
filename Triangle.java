import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class Triangle {
	//I'm not even trying to catch exceptions and display pretty error messages
	//for this problem - I'll just let the exception bubble up
	public static void main(String[] args) throws IOException {
		//make sure that the program was passed the datafile as a CL argument
		if(args.length != 1) {
			System.out.print("Please pass exactly one command line argument: " +
					"the pathname of the data file (triangle.txt)");
			System.exit(1);
		}
		
		//read in the data
		File dataFile = new File(args[0]);
		Deque<int[]> parsedData = parseData(dataFile);
		
		//repeatedly reduce the problem's complexity by 1 row, until it's trivial
		while(parsedData.size() > 1) {
			reduceTriangle(parsedData); //reduceTriangle is destructive and modifies in-place
		}
		
		//print the solution to the 'trivial problem' (triangle with only one element)
		System.out.println("The max sum is " + parsedData.remove()[0]);
	}
	
	//I'm using Dequeus since I'll be often removing the head element
	private static Deque<int[]> parseData(File f) throws IOException {
		Deque<int[]> parsed = new LinkedList<int[]>();
		BufferedReader reader = new BufferedReader(new FileReader(f));

		//it would probably be faster (runtime wise) to write a finite state machine to parse the triangle.txt file
		//but it's not really worth optimizing for (w.r.t. the added code complexity) unless it's needed
		String currentLine;
		int currentLineSize = 0;
		while((currentLine = reader.readLine()) != null) {
			String[] splitLine = currentLine.split("\\s+");
			int[] lineData = new int[splitLine.length];
			
			//verify that the current line is one element larger than the previous (i.e., we're really dealing with a triangle)
			assert(splitLine.length == ++currentLineSize);
			
			for(int i = 0; i < splitLine.length; i++) {
				lineData[i] = Integer.parseInt(splitLine[i]);
			}
			parsed.push(lineData);
		}
		
		return parsed;
	}
	
	//just work the problem backwards, and figure out the 'best'
	//solution for the second to last row (thereby reducing the complexity of
	//the problem by one row).
	private static Deque<int[]> reduceTriangle(Deque<int[]> triangle) {
		int[] lastRow = triangle.pop();
		int[] secondToLastRow = triangle.pop();
		
		for(int i = 0; i < secondToLastRow.length; i++) {
			secondToLastRow[i] = Math.max(secondToLastRow[i] + lastRow[i], secondToLastRow[i] + lastRow[i+1]);
		}
		triangle.push(secondToLastRow);
		return triangle;
	}
}