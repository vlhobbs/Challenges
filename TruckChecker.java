/*
v1.0, written in Fall 2016. This code is a mess, but it works. Uploading it to practice with git and to learn how to "clean up" code.
*/

import java.util.ArrayList;
import java.util.List;


public class TruckChecker{

	public static void main(String[] args) {
		//Add some trucks, create a fleet list
		//Entering the tuples as list of strings
		//That is how they would be entered in a java text field
		//so it's good to figure out how to convert them now

		ArrayList<String> tuples = new ArrayList<String>();
		tuples.add("(1900, 1950)");
		tuples.add("(1910, 1940)");  
		tuples.add("(1905, 1965)");
		tuples.add("(1960, 1980)");
		tuples.add("(1825, 1876)");
		tuples.add("(1919, 1922)");

		ArrayList<Tuple> convertedTuples = new ArrayList<Tuple>();
		for (String oldTuple : tuples){
			convertedTuples.add(new Tuple(oldTuple));
		}
		
		//A small fleet of improbable trucks
		
		Truck truck0 = new Truck(0, 1950, 2000);
		Truck truck1 = new Truck(1, 1920, 1960);
		Truck truck2 = new Truck(2, 1958, 1960);
		Truck truck3 = new Truck(3, 1875, 1930);

		ArrayList<Truck> myFleet = new ArrayList<Truck>();
		myFleet.add(truck0);
		myFleet.add(truck1);
		myFleet.add(truck2);
		myFleet.add(truck3);

		ArrayList<YearList> years = new ArrayList<YearList>();

		//Since I know the greatest year range, I'm going to add them directly
		//For a larger year range, getEarliestYear and getLatestYear would save memory
		//But for a relatively small range this is a good programming-time-saving method

		for (int i = 1800; i<= 2000; i++){
			years.add(new YearList(i));
		}

		//For each truck ->
		//For each year in the list (1800-2000):\ ->
		//See if that truck existed in that year
		//If yes, increment that year's number of trucks
		//This part will take longer (n^2 worst case!) for larger lists

		for (Truck aTruck : myFleet)
		{
			for (YearList year : years)
			{
				if (year.getYear() >= aTruck.getStartYear() && year.getYear() <= aTruck.getEndYear()){
					year.addTruck();
				}
			}

		}


		for (Tuple newTuple : convertedTuples){
			yearWithMostTrucks(newTuple.getNum1(), newTuple.getNum2(), years);
			yearWithLeastTrucks(newTuple.getNum1(), newTuple.getNum2(), years);
		}
		System.out.println("-----");

		for (Tuple newTuple : convertedTuples){
			topNYearsWithMostTrucks(newTuple.getNum1(), newTuple.getNum2(), years, 3);
		}
		System.out.println("-----");

		for (Tuple newTuple : convertedTuples){
			topNYearsWithLeastTrucks(newTuple.getNum1(), newTuple.getNum2(), years, 5);
		}
	}
	//Methods
	//Using subsets to search specific year ranges
	//The start and end values are the tuples; the indices are the tuple values - 1800
	//Original method involved a search algorithm that iterated over every YearList object
	//Bubble Sort, while slow, is at least faster than this

	public static void yearWithMostTrucks(int start, int end, ArrayList<YearList> years){
		List<YearList> subList = years.subList(start - 1800,  end - 1799);
		ArrayList<YearList> subArrayList = new ArrayList<YearList>();
		for (YearList year : subList){
			subArrayList.add(year);
		}
		sortByTrucks(subArrayList);
		System.out.printf("Date Range %d - %d: Year with the most trucks: %d (%d trucks)\n", start, end, subArrayList.get(subArrayList.size() - 1).getYear(), subArrayList.get(subArrayList.size()-1).truckCount());
	}		

	public static void yearWithLeastTrucks(int start, int end, ArrayList<YearList> years){
		List<YearList> subList = years.subList(start - 1800,  end - 1799);
		ArrayList<YearList> subArrayList = new ArrayList<YearList>();
		for (YearList year : subList){
			subArrayList.add(year);
		}
		sortByTrucks(subArrayList);
		System.out.printf("Date Range %d - %d: Year with the fewest trucks: %d (%d trucks)\n", start, end, subArrayList.get(0).getYear(), subArrayList.get(0).truckCount());
	}
	//Using lists for the top lists - that way I can use sublists to easily separate them
	
	public static void topNYearsWithMostTrucks(int start, int end, ArrayList<YearList> years, int n){
		try{
			ArrayList<YearList> subYear = new ArrayList<YearList>();
			List<YearList> subYearList = years.subList(start - 1800,  end-1799);
			for (YearList year: subYearList){
				subYear.add(year);
			}
			sortByTrucks(subYear);
			List<YearList> sorted = subYear.subList(subYear.size()-n, subYear.size());

			System.out.printf("Top %d years with trucks between %d and %d:\n", n, start, end);
			for (int i = n-1; i >=0; i--){
				System.out.printf("%d (%d trucks), ",sorted.get(i).getYear(), sorted.get(i).truckCount());
			}
			System.out.printf("End of list.\n");
		} catch(Exception E) {
			System.out.println("There are not enough years in this list to show the top "+ n + " years!");
		}
	}

	public static void topNYearsWithLeastTrucks(int start, int end, ArrayList<YearList> years, int n){
		try{
			ArrayList<YearList> subYear = new ArrayList<YearList>();
			List<YearList> subYearList = years.subList(start - 1800,  end-1799);
			for (YearList year: subYearList){
				subYear.add(year);
			}
			sortByTrucks(subYear);
			List<YearList> sorted = subYear.subList(0, n);
			System.out.printf("Top %d years with fewest trucks between %d and %d:\n", n, start, end);
			for (int i = n-1; i >=0; i--){
				System.out.printf("%d (%d trucks), ",sorted.get(i).getYear(), sorted.get(i).truckCount());
			}
			System.out.printf("End of list.\n");
		} catch(Exception E) {
			System.out.println("There are not enough years in this list to show the bottom "+ n + " years!");
		}		
	}

	//Comparator needed to sort
	
	public static boolean compareTrucks(YearList year1, YearList year2){
		//return true if year1 has more trucks than year2
		return (year1.truckCount() > year2.truckCount());
	}

	//Bubble sort algorithm; slow (at least n^2) but effective
	//Would try to use more efficient sort for a larger-scale program
	
	public static void sortByTrucks(ArrayList<YearList> years){
		YearList tmp;
		for (int i = 0; i < years.size() - 1; i++){
			for (int j = i; j < years.size() - 1 ; j++){
				if (compareTrucks(years.get(j), years.get(j+1))){
					tmp = years.get(j);
					years.set(j, years.get(j+1) );
					years.set(j+1,  tmp);
				}
			}
		}

		/*Created these methods to make custom date ranges, but did not have time to implement fully
		 * Here they are for the record

	public static int getEarliestDate(ArrayList<Truck> truckList){
		int earliestDate = 9999;
		for (Truck aTruck : truckList){
			if (aTruck.getStartYear() < earliestDate){
				earliestDate = aTruck.getStartYear();
			}
		}
		return earliestDate;
	}

	public static int getLatestDate(ArrayList<Truck> truckList){
		int latestDate = 0;
		for (Truck aTruck : truckList){
			if (aTruck.getEndYear() > latestDate){
				latestDate = aTruck.getEndYear();
			}
		}
		return latestDate;
	}
		 */

		//Class Tuple
		//Specifically made to convert string list of tuples to pairs of numbers
	}
	static class Tuple{
		private int num1;
		private int num2;

		public Tuple(){
			num1 = 0;
			num2 = 0;
		}

		public Tuple(String s){
			//This assumes the exact values as above.
			//Nine characters, first four are ints, last four are ints
			//A more rigorous design would use a try/catch block here
			//in case someone decided to enter wrong data
			num1 = Integer.parseInt(s.substring(1,5));
			num2 = Integer.parseInt(s.substring(7,11));
		}

		public int getNum1(){
			return num1;
		}
		public int getNum2(){
			return num2;
		}
	}

	//Class YearList - contains a year (identifier) and a number of Trucks in the year
	//Originally planned to have a list of Truck IDs, but that was a bit too much to handle.
	static class YearList{
		private int year;
		private int numTrucks;


		public YearList(){
			year = 0;
			numTrucks =0;
		}
		public YearList(int num){
			year = num;
			numTrucks = 0;

		}

		public int getYear(){
			return year;
		}

		public void addTruck(){
			numTrucks +=1;
		}
		public void setTrucks(int num){
			numTrucks = num;
		}
		public int truckCount(){
			return numTrucks;
		}
		public void countTrucks(Truck truck){
			if (year >= truck.getStartYear() && year <= truck.getEndYear()){
				numTrucks ++;
			}
		}
	}
	static class Truck{
		//Variables: ID number, year they first operated, year they last operated
		//ID number would be useful for later identification but is not currently used
		private int idNum;
		private int startYear;
		private int endYear;
		private int[] yearRange;

		//Constructor
		public Truck(int id, int start, int end)
		{
			idNum = id;
			startYear = start;
			endYear = end;
			yearRange = this.generateRange();
		}

		public int getStartYear(){
			return startYear;
		}

		public int getEndYear(){
			return endYear;
		}

		//Generates the full range of years the truck was active
		//Used to populate the ArrayLists of YearList objects
		
		public int[] generateRange(){
			int total = (endYear - startYear) + 1;
			int[] range;
			range = new int[total];
			for (int i = 0; i + startYear <= endYear; i++){
				range[i] = startYear + i;
			}
			return range;
		}	
		public int[] getRange(){
			return yearRange;
		}
	}
}
