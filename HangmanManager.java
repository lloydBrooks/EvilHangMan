/*HangmanManager.java
  Lloyd Brooks
  10/6/2016
  CS 145 Programming assignment 3
  
  This program will...
*/

import java.util.*;//gives this program acess to collections


public class HangmanManager
{
   //initialized variables
   private int occurrences;
   private String pattern;//the current word pattern with the guessed letters revealed
   private SortedSet<Character> guesses=new TreeSet<Character>();//a list of guessed letters 
   private Set<String> words=new TreeSet<String>();//the words that are not yet eliminated
   private int wordLength;//the length of word designated by the user
   private int tries;//the number of wrong guesses remainng to guess the word
   private Map<String, ArrayList<String>> myMap=new TreeMap<String, ArrayList<String>>();//used to store the potential words and letter patterns after each guess
   
   //the constructor for our HangmanManager
   public HangmanManager(List<String> dictonary, int length, int max) throws IllegalArgumentException
   {
      if(length<1 || max<0)
      {
         throw new IllegalArgumentException();
      }
      else{
         wordLength=length;//sets the target word length
         tries=max;//sets the number of tries to the max to start. will be reduced every time a wrong letter is guessed
         Iterator<String> itr=dictonary.iterator();
         
         pattern="-";
         //creates the pattern with go letters guessed
         for(int i=1;i<wordLength;i++)
         {
            pattern=pattern+'-';
         }
         
         //puts all words of the given lenght into the words set if they are not already there
      
         while(itr.hasNext())         
         {
            String temp=itr.next();
            if(temp.length()==wordLength)
            {
               words.add(temp);
            }
         }  
      }
      
   }//end constructor
   
   //used to access to current set of potential words
   public Set<String> words()
   {
      return words;
   }//end words()
   
   //reports the number of guesses remaining
   public int guessesLeft()
   {
      return tries;
   }//end GuessesLeft()
   
   //reports the set of letters that have been guessed
   public SortedSet<Character> guesses()
   {
      return guesses;
   }//end Guesses()
   
   //returns the pattern for the word
   public String pattern() throws IllegalStateException
   {
      if(pattern.isEmpty())
      {
         throw new IllegalStateException();
      }
      
      return pattern;
   }//end pattern()
   
   //records guessed letter, decides what pattern to choose, returns: nuber of occurrences of 
   //the guessed letter in the pattern, and it updates the number of guesses accordingly
   public int record(char guess)throws IllegalStateException, IllegalArgumentException
   {
      myMap.clear();//clears the map so it can be used with the newest guess
      String keeper=" ";//used to temporarily store the word patterns while populating myMap and to store the best key later
      ArrayList<String> tempArray=new ArrayList<String>();//used to add words to myMap and to store words from a key later
      occurrences=0;//reset occurrences for current guess
      
      if(tries<1)
      {
         throw new IllegalStateException("out of tries");
      }
      if(guesses.contains(guess))
      {
         throw new IllegalArgumentException("That letter has already been guessed");
      }
      
      guesses.add(guess);//adds the guessed letter to the set of guesses
      
      for(String word: words)
      {
         keeper=" ";//clears the keeper string so it can be used to create a pattern for the new word
         tempArray.clear();//clears the temp array for the new word
         
         for(int i=0;i<word.length();i++)
         {
            char letter=word.charAt(i);
            if(guesses.contains(letter))
            {
               keeper=keeper+ letter;
            }else{
               keeper=keeper+'-';
            }
         }//end of for each letter loop
         
         if(!myMap.containsKey(keeper))
         {
            tempArray.add(word);
            myMap.put(keeper,tempArray);
         }else{//the key is already part of the map
            tempArray=myMap.get(keeper);
            tempArray.add(word);
            myMap.put(keeper,tempArray);
         }         
      }//end of for each word loop
      int largest=0;//used to keep track of the pattern with the largest number of wods
      for(String key: myMap.keySet())
      {
         
         if(myMap.get(key).size()>largest)
         {
            largest=myMap.get(key).size();
            keeper=key;
         }
         
      }//end of for each key loop
      for(int i=0;i<keeper.length();i++)
      {
         char letter=keeper.charAt(i);
         if(letter==guess)
         {
            occurrences++;
         }
      }//end fo for each letter in keeper loop
      if(occurrences<1)
      {
         tries--;
      }
      pattern=keeper;
      
      tempArray.clear();
      //updates the set of words being considered
      for(String word: words)
      {
         for(int i=0;i<wordLength;i++)
         {
            if(guesses.contains(pattern.charAt(i)))
            {
               if(word.charAt(i)!=pattern.charAt(i))
               {
                  tempArray.add(word);
               }
            }
         }
      }//end of for each word loop
      for(String word:tempArray)
      {
         words.remove(word);
      }
      
      
      return occurrences;
      
   }//end record
   
}//end HangmanManager