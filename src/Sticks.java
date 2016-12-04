//
// Title:            Program 2 (Sticks)
// Files:            Config.java, Sticks.java, TestSticks.java
// Semester:         Fall 2016
//
// Author:           Ellen Krebs
// Email:            ekrebs@wisc.edu
// CS Login:         ekrebs
// Lecturer's Name:  Gary Dahl
// Lab Section:      345

// Outside help:
// Tutors in CSLC, they specifically helped me with creating the aiChooseAction 
//method and the strategyTable
// Multiple Piazza posts for each milestone

import java.util.Arrays;
import java.util.Scanner;

/**
 * The class Sticks contains a game where two players, a friend or computer,
 * grab a certain number of sticks within a specified range (Config.MIN_ACTION -
 * Config.MAX_ACTION) until they are completely gone. The user decides how many
 * sticks there will be initially (Config.MIN_STICKS - Config.MAX_STICKS), and
 * then they will decide whether they want to play against a friend, computer
 * (basic, no AI) or against a computer with AI
 * 
 * @author Ellen Krebs
 *
 */

public class Sticks {

	/**
	 * This is the main method for the game of Sticks. In milestone 1 this
	 * contains the whole program for playing against a friend. In milestone 2
	 * this contains the welcome, name prompt, how many sticks question, menu,
	 * calls appropriate methods and the thank you message at the end. One
	 * method called in multiple places is promptUserForNumber. When the menu
	 * choice to play against a friend is chosen, then playAgainstFriend method
	 * is called. When the menu choice to play against a computer is chosen,
	 * then playAgainstComputer method is called. If the computer with AI option
	 * is chosen then trainAI is called before calling playAgainstComputer.
	 * Finally, call strategyTableToString to prepare a strategy table for
	 * printing.
	 * 
	 * @param args
	 *            (unused)
	 */
	public static void main(String[] args) {
		// List of my variables

		// Scanner variable to take in user's input
		Scanner input = new Scanner(System.in);

		// Variable that stores the name of the user
		String userName;

		// Number of initial sticks that the user decides to put on the board
		int numOfInitialSticks;

		// Variable that stores which menu choice that the user chooses
		int menuChoice;

		// Name of the friend in milestone 1
		String friendName;

		// Number of games the AI wants to learn from
		int numOfGames;

		// User's input
		String userInput;

		// User's input as a character
		char userInputChar;

		// This displays the welcome statement.
		System.out.println("Welcome to the Game of Sticks!");
		System.out.println("==============================");
		System.out.println("");

		// This code asks the user to input their name. Then, a statement is
		// printed out saying "Hello" + whatever the
		// user's name is.
		System.out.print("What is your name? ");
		userName = input.nextLine();
		System.out.println("Hello " + userName.trim() + ".");

		// This statement asks the user to input the number of sticks available
		// to take.
		numOfInitialSticks = promptUserForNumber(input,
				"How many sticks are there on the table initially ("
						+ Config.MIN_STICKS + "-" + Config.MAX_STICKS + ")? ",
				Config.MIN_STICKS, Config.MAX_STICKS);

		// The number of initial sticks is assigned to the number of sticks that
		// are left.

		// This code prints out the main menu of the game. The game asks whether
		// the user would like to play against
		// a friend, a computer, or a computer with an AI. If the user inputs
		// the number 1, then they will play against a
		// friend. If they input the number 2, then they will play against a
		// computer. If the user inputs the number 3,
		// then they will play against a computer with an AI.
		System.out.println("");
		System.out.println("Would you like to:");
		System.out.println(" 1) Play against a friend");
		System.out.println(" 2) Play against computer (basic)");
		System.out.println(" 3) Play against computer with AI");

		menuChoice = promptUserForNumber(input, "Which do you choose (1,2,3)? ",
				Config.MIN_ACTION, 3);

		// If the user decides to input the number 1, then they will play
		// against a friend. Their choice prompts
		// the user to input their friend's name. Then, the playAgainstFriend
		// method is called
		if (menuChoice == 1) {
			System.out.println("");
			System.out.print("What is your friend's name? ");
			friendName = input.nextLine();
			System.out.println("Hello " + friendName.trim() + ".");
			System.out.println("");

			// The method to play the game of sticks against a friend is called
			playAgainstFriend(input, numOfInitialSticks, userName, friendName);

			// When the user wants to play against a computer
		} else if (menuChoice == 2) {
			playAgainstComputer(input, numOfInitialSticks, userName, null);

			// When the user wants to play against the AI
		} else if (menuChoice == 3) {
			System.out.println("");

			// Asks for how many games the AI should learn from
			numOfGames = promptUserForNumber(input,
					"How many games should the AI learn from ("
							+ Config.MIN_GAMES + " to " + Config.MAX_GAMES
							+ ")? ",
					Config.MIN_GAMES, Config.MAX_GAMES);

			int[][] strategyTable = trainAi(numOfInitialSticks, numOfGames);

			// Game against AI is played
			playAgainstComputer(input, numOfInitialSticks, userName,
					strategyTable);

			// Strategy table is printed out
			System.out
					.print("Would you like to see the strategy table (Y/N)? ");
			userInput = input.nextLine();
			userInputChar = userInput.charAt(0);

			if (userInputChar == 'Y' || userInputChar == 'y') {
				System.out.print(strategyTableToString(strategyTable));
			}
		}

		// This prints the closing statement
		System.out.println("");
		System.out.println("=========================================");
		System.out.println("Thank you for playing the Game of Sticks!");
		input.close();
	}

	/**
	 * This method encapsulates the code for prompting the user for a number and
	 * verifying the number is within the expected bounds.
	 * 
	 * @param input
	 *            The instance of the Scanner reading System.in.
	 * @param prompt
	 *            The prompt to the user requesting a number within a specific
	 *            range.
	 * @param min
	 *            The minimum acceptable number.
	 * @param max
	 *            The maximum acceptable number.
	 * @return The number entered by the user between and including min and max.
	 */
	static int promptUserForNumber(Scanner input, String prompt, int min,
			int max) {

		// A variable storing the user's input
		int userInput;

		// Stores the value if the user inputs a string or character(s)
		String invalidChoice;

		System.out.print(prompt);
		while (true) {

			// If the user does not put in an integer, then it returns and error
			// message and asks for the user
			// to input an answer again until a valid number is put in.
			if (!input.hasNextInt()) {
				invalidChoice = input.nextLine();
				System.out.println("Error: expected a number between " + min
						+ " and " + max + " but found: " + invalidChoice);
				System.out.print(prompt);
			} else {
				userInput = input.nextInt();
				input.nextLine();

				// If the user inputs a number outside the bounds, then a
				// statement is printed, asking the user
				// to enter a number between the specified minimum and maximum
				// parameters until one is put in.
				if (userInput < min || userInput > max) {
					System.out.println("Please enter a number between " + min
							+ " and " + max + ".");
					System.out.print(prompt);
				} else {
					break;
				}
			}
		}

		return userInput;

	}

	/**
	 * This method has one person play the Game of Sticks against another
	 * person.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1Name
	 *            The name of one player.
	 * @param player2Name
	 *            The name of the other player.
	 * 
	 *            As a courtesy, player2 is considered the friend and gets to
	 *            pick up sticks first.
	 * 
	 */
	static void playAgainstFriend(Scanner input, int startSticks,
			String player1Name, String player2Name) {
		System.out
				.println("There are " + startSticks + " sticks on the board.");

		// Stores the number of sticks that are taken
		int numOfSticksTaken;

		// These strings store each prompt that asks a player how many sticks
		// they want to take
		String sticksTakenPrompt1 = ": " + "How many sticks do you take ("
				+ Config.MIN_ACTION + "-" + Config.MAX_ACTION + ")? ";
		String sticksTakenPrompt2 = ": " + "How many sticks do you take ("
				+ Config.MIN_ACTION + "-" + 2 + ")? ";
		String sticksTakenPrompt3 = ": " + "How many sticks do you take ("
				+ Config.MIN_ACTION + "-" + Config.MIN_ACTION + ")? ";

		// This is the friend's turn
		boolean isUserNameTurn = false;

		while (startSticks > 0) {
			// If isUserNameTurn == false, then it is the friend's turn
			if (isUserNameTurn == false) {

				// If the number of sticks left is greater than 2:
				if (startSticks > 2) {
					numOfSticksTaken = promptUserForNumber(input,
							player2Name.trim() + sticksTakenPrompt1,
							Config.MIN_ACTION, Config.MAX_ACTION);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks left is 2:
				else if (startSticks == 2) {
					numOfSticksTaken = promptUserForNumber(input,
							player2Name.trim() + sticksTakenPrompt2,
							Config.MIN_ACTION, 2);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks left is 1:
				else if (startSticks == 1) {
					numOfSticksTaken = promptUserForNumber(input,
							player2Name.trim() + sticksTakenPrompt3,
							Config.MIN_ACTION, Config.MIN_ACTION);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is greater than 2, then it switches
				// to
				// the user's turn
				if (startSticks > 2 && isUserNameTurn == false) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = true;

					// If there are 2 sticks left then it switches to the user's
					// turn
				} else if (startSticks == 2 && isUserNameTurn == false) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = true;

					// If there is only 1 stick left then it switches to the
					// user's turn
				} else if (startSticks == Config.MIN_ACTION
						&& isUserNameTurn == false) {
					System.out.println(
							"There is " + startSticks + " stick on the board.");
					isUserNameTurn = true;
				} else {
					break;
				}

			}
			// This is the user's turn
			if (isUserNameTurn == true) {

				// If the number of sticks is greater than 2:
				if (startSticks > 2) {
					numOfSticksTaken = promptUserForNumber(input,
							player1Name.trim() + sticksTakenPrompt1,
							Config.MIN_ACTION, Config.MAX_ACTION);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is 2:
				else if (startSticks == 2) {
					numOfSticksTaken = promptUserForNumber(input,
							player1Name.trim() + sticksTakenPrompt2,
							Config.MIN_ACTION, 2);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is 1
				else if (startSticks == Config.MIN_ACTION) {
					numOfSticksTaken = promptUserForNumber(input,
							player1Name.trim() + sticksTakenPrompt3,
							Config.MIN_ACTION, Config.MIN_ACTION);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is greater than 2, then it switches
				// to
				// the friend's turn
				if (startSticks > 2 && isUserNameTurn == true) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = false;

					// If the number of sticks is 2, then it switches to the
					// friend's turn
				} else if (startSticks == 2 && isUserNameTurn == true) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = false;

					// If the number of sticks is 1, then it switches to the
					// friend's turn
				} else if (startSticks == Config.MIN_ACTION
						&& isUserNameTurn == true) {
					System.out.println(
							"There is " + startSticks + " stick on the board.");
					isUserNameTurn = false;
				} else {
					break;
				}

			}
		}

		// Will run if there are no sticks left and it's the friend's turn:
		if (startSticks == 0 && isUserNameTurn == true) {
			System.out.println(player2Name.trim() + " wins." + " "
					+ player1Name.trim() + " loses.");
		}
		// Will run if there are no sticks left and it's the user's turn:
		else if (startSticks == 0 && isUserNameTurn == false) {
			System.out.println(player1Name.trim() + " wins." + " "
					+ player2Name.trim() + " loses.");
		}

	}

	/**
	 * Make a choice about the number of sticks to pick up when given the number
	 * of sticks remaining.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining,
	 * then pick up the minimum number of sticks (Config.MIN_ACTION). If
	 * Config.MAX_ACTION sticks remain, randomly choose a number between
	 * Config.MIN_ACTION and Config.MAX_ACTION. Use Config.RNG.nextInt(?) method
	 * to generate an appropriate random number.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining in the game.
	 * @return The number of sticks to pick up, or 0 if sticksRemaining is <= 0.
	 */
	static int basicChooseAction(int sticksRemaining) {

		if (sticksRemaining <= 0) {
			return 0;
		} else if (sticksRemaining < Config.MAX_ACTION) {
			return Config.MIN_ACTION;
		} else if (sticksRemaining >= Config.MAX_ACTION) {
			return Config.RNG.nextInt(Config.MAX_ACTION) + Config.MIN_ACTION;
		}

		return sticksRemaining;
	}

	/**
	 * This method has a person play against a computer. Call the
	 * promptUserForNumber method to obtain user input. Call the aiChooseAction
	 * method with the actionRanking row for the number of sticks remaining.
	 * 
	 * If the strategyTable is null, then this method calls the
	 * basicChooseAction method to make the decision about how many sticks to
	 * pick up. If the strategyTable parameter is not null, this method makes
	 * the decision about how many sticks to pick up by calling the
	 * aiChooseAction method.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param playerName
	 *            The name of one player.
	 * @param strategyTable
	 *            An array of action rankings. One action ranking for each stick
	 *            that the game begins with.
	 * 
	 */
	static void playAgainstComputer(Scanner input, int startSticks,
			String playerName, int[][] strategyTable) {
		System.out
				.println("There are " + startSticks + " sticks on the board.");

		// This boolean variable is true when it's the user's turn to go
		boolean isUserNameTurn = true;

		// This variable stores the value of the number of sticks that are taken
		int numOfSticksTaken;

		// These strings store each prompt that asks a player how many sticks
		// they want to take
		String sticksTakenPrompt1 = ": " + "How many sticks do you take ("
				+ Config.MIN_ACTION + "-" + Config.MAX_ACTION + ")? ";
		String sticksTakenPrompt2 = ": " + "How many sticks do you take ("
				+ Config.MIN_ACTION + "-" + 2 + ")? ";
		String sticksTakenPrompt3 = ": " + "How many sticks do you take ("
				+ Config.MIN_ACTION + "-" + Config.MIN_ACTION + ")? ";

		while (startSticks > 0) {
			// This is the user's turn
			if (isUserNameTurn == true) {

				// If the number of sticks is greater than 2:
				if (startSticks > 2) {
					numOfSticksTaken = promptUserForNumber(input,
							playerName.trim() + sticksTakenPrompt1,
							Config.MIN_ACTION, Config.MAX_ACTION);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is 2:
				else if (startSticks == 2) {
					numOfSticksTaken = promptUserForNumber(input,
							playerName.trim() + sticksTakenPrompt2,
							Config.MIN_ACTION, 2);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is 1
				else if (startSticks == Config.MIN_ACTION) {
					numOfSticksTaken = promptUserForNumber(input,
							playerName.trim() + sticksTakenPrompt3,
							Config.MIN_ACTION, Config.MIN_ACTION);
					startSticks = startSticks - numOfSticksTaken;
				}

				// If the number of sticks is at least 3, then it switches to
				// the computer's turn
				if (startSticks > 2 && isUserNameTurn == true) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = false;

					// If the number of sticks is 2, then it switches to the
					// computer's turn
				} else if (startSticks == 2 && isUserNameTurn == true) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = false;

					// If the number of sticks is 1, then it switches to the
					// computer's turn
				} else if (startSticks == Config.MIN_ACTION
						&& isUserNameTurn == true) {
					System.out.println(
							"There is " + startSticks + " stick on the board.");
					isUserNameTurn = false;
				} else {
					break;
				}
			}

			// If isUserNameTurn == false, then it is the computer's turn
			if (isUserNameTurn == false) {

				// If the strategyTable is null, then basicChooseAction will be
				// run
				if (strategyTable == null) {

					// If the number of sticks left is greater than 2:
					if (startSticks > 2) {
						numOfSticksTaken = basicChooseAction(startSticks);
						if (numOfSticksTaken > Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " sticks.");
						} else if (numOfSticksTaken == Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " stick.");
						}
						startSticks = startSticks - numOfSticksTaken;
					}

					// If the number of sticks left is 2:
					else if (startSticks == 2) {
						numOfSticksTaken = basicChooseAction(startSticks);
						if (numOfSticksTaken > Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " sticks.");
						} else if (numOfSticksTaken == Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " stick.");
						}
						startSticks = startSticks - numOfSticksTaken;
					}

					// If the number of sticks left is 1:
					else if (startSticks == Config.MIN_ACTION) {
						numOfSticksTaken = basicChooseAction(startSticks);
						System.out.println("Computer selects "
								+ numOfSticksTaken + " stick.");
						startSticks = startSticks - numOfSticksTaken;
					}

					// If the strategy table is not null, then aiChooseAction is
					// called
				} else if (strategyTable != null) {
					// If the number of sticks left is greater than 2:
					if (startSticks > 2) {
						numOfSticksTaken = aiChooseAction(startSticks,
								strategyTable[startSticks - 1]);
						if (numOfSticksTaken > Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " sticks.");
						} else if (numOfSticksTaken == Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " stick.");
						}
						startSticks = startSticks - numOfSticksTaken;
					}

					// If the number of sticks left is 2:
					else if (startSticks == 2) {
						numOfSticksTaken = aiChooseAction(startSticks,
								strategyTable[startSticks - 1]);
						if (numOfSticksTaken > Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " sticks.");
						} else if (numOfSticksTaken == Config.MIN_ACTION) {
							System.out.println("Computer selects "
									+ numOfSticksTaken + " stick.");
						}
						startSticks = startSticks - numOfSticksTaken;
					}

					// If the number of sticks left is 1:
					else if (startSticks == Config.MIN_ACTION) {
						numOfSticksTaken = aiChooseAction(startSticks,
								strategyTable[startSticks - 1]);
						System.out.println("Computer selects "
								+ numOfSticksTaken + " stick.");
						startSticks = startSticks - numOfSticksTaken;
					}
				}

				// If the number of sticks is at least 3, then it switches to
				// the user's turn
				if (startSticks > 2 && isUserNameTurn == false) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = true;

					// If there are 2 sticks left then it switches to the user's
					// turn
				} else if (startSticks == 2 && isUserNameTurn == false) {
					System.out.println("There are " + startSticks
							+ " sticks on the board.");
					isUserNameTurn = true;

					// If there is only 1 stick left then it switches to the
					// user's turn
				} else if (startSticks == Config.MIN_ACTION
						&& isUserNameTurn == false) {
					System.out.println(
							"There is " + startSticks + " stick on the board.");
					isUserNameTurn = true;
				} else {
					break;
				}

			}
		}
		// Will run if there are no sticks left and it's the user's turn:
		if (startSticks == 0 && isUserNameTurn == true) {
			System.out.println(
					"Computer wins." + " " + playerName.trim() + " loses.");
		}
		// Will run if there are no sticks left and it's the user's turn:
		else if (startSticks == 0 && isUserNameTurn == false) {
			System.out.println(
					playerName.trim() + " wins." + " " + "Computer loses.");
		}

	}

	/**
	 * This method chooses the number of sticks to pick up based on the
	 * sticksRemaining and actionRanking parameters.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining then
	 * the chooser must pick the minimum number of sticks (Config.MIN_ACTION).
	 * For Config.MAX_ACTION or more sticks remaining then pick based on the
	 * actionRanking parameter.
	 * 
	 * The actionRanking array has one element for each possible action. The 0
	 * index corresponds to Config.MIN_ACTION and the highest index corresponds
	 * to Config.MAX_ACTION. For example, if Config.MIN_ACTION is 1 and
	 * Config.MAX_ACTION is 3, an action can be to pick up 1, 2 or 3 sticks.
	 * actionRanking[0] corresponds to 1, actionRanking[1] corresponds to 2,
	 * etc. The higher the element for an action in comparison to other
	 * elements, the more likely the action should be chosen.
	 * 
	 * First calculate the total number of possibilities by summing all the
	 * element values. Then choose a particular action based on the relative
	 * frequency of the various rankings. For example, if Config.MIN_ACTION is 1
	 * and Config.MAX_ACTION is 3: If the action rankings are {9,90,1}, the
	 * total is 100. Since actionRanking[0] is 9, then an action of picking up 1
	 * should be chosen about 9/100 times. 2 should be chosen about 90/100 times
	 * and 1 should be chosen about 1/100 times. Use Config.RNG.nextInt(?)
	 * method to generate appropriate random numbers.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining to be picked up.
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @return The number of sticks to pick up. 0 is returned for the following
	 *         conditions: actionRanking is null, actionRanking has a length of
	 *         0, or sticksRemaining is <= 0.
	 * 
	 */
	static int aiChooseAction(int sticksRemaining, int[] actionRanking) {
		// Sum of the elements in the actionRanking array
		int sumOfElements = 0;

		// This new sum adds up elements in actionRanking at an index
		int newSum = 0;

		// Stores the number of sticks to take
		int sticksToTake = 0;

		// If any of these conditions are true, then 0 is returned
		if (actionRanking == null || actionRanking.length == 0
				|| sticksRemaining <= 0) {
			return 0;

			// If the number of sticks remaining is less than the maximum number
			// of sticks a player can take, then the minimum number of sticks a
			// player can take is returned
		} else if (sticksRemaining < Config.MAX_ACTION) {
			return Config.MIN_ACTION;

			// If the number of sticks remaining is greater than the maximum
			// number of sticks a player can take
		} else if (sticksRemaining >= Config.MAX_ACTION) {

			// All the elements in the actionRanking array are summed up
			for (int i = 0; i < actionRanking.length; i++) {

				sumOfElements += actionRanking[i];
			}
			// This generates appropriate random numbers based on the
			// sumOfElements

			int random = Config.RNG.nextInt(sumOfElements) + 1;

			do {
				newSum += actionRanking[sticksToTake];
				sticksToTake++;
			} while (newSum < random);
		}
		return sticksToTake;

	}

	/**
	 * This method initializes each element of the array to 1. If actionRanking
	 * is null then method simply returns.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 */
	static void initializeActionRanking(int[] actionRanking) {

		if (actionRanking == null) {
			return;
		}

		// Initializes each element in actionRanking to 1
		for (int i = 0; i < actionRanking.length; i++) {
			actionRanking[i] = 1;
		}
	}

	/**
	 * This method returns a string with the number of sticks left and the
	 * ranking for each action as follows.
	 * 
	 * An example: 10 3,4,11
	 * 
	 * The string begins with a number (number of sticks left), then is followed
	 * by 1 tab character, then a comma separated list of rankings, one for each
	 * action choice in the array. The string is terminated with a newline (\n)
	 * character.
	 * 
	 * @param sticksLeft
	 *            The number of sticks left.
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 * @return A string formatted as described.
	 */
	static String actionRankingToString(int sticksLeft, int[] actionRanking) {

		// Will store a number at an index inside of actionRanking and change
		// the number to a string
		String actionRanking1 = "";

		// Changes sticksLeft int into a string
		String sticksLeft1 = Integer.toString(sticksLeft);

		// Variable for storing the number of sticks left and their rankings
		String sticksLeftRanking;

		for (int i = 0; i < actionRanking.length; i++) {
			// if i is at the last index of the actionRanking array
			if (i == actionRanking.length - 1) {
				// sets the integer inside index i to a string
				actionRanking1 += Integer.toString(actionRanking[i]);
			} else {
				// If i is not at the last index, set the integer inside index i
				// to a string and add a
				// comma on the end of it
				actionRanking1 += Integer.toString(actionRanking[i]) + ",";
			}
		}
		// Starts with number of sticks left, followed by a tab character, then
		// separates each of the rankings
		sticksLeftRanking = sticksLeft1 + "\t" + actionRanking1 + "\n";

		return sticksLeftRanking;
	}

	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was lost, the actionRanking for the action is decremented by 1, but not
	 * allowing the value to go below 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnLoss(int[] actionRanking, int action) {

		actionRanking[action - Config.MIN_ACTION]--;
		if (actionRanking[action - Config.MIN_ACTION] < 1) {
			actionRanking[action - Config.MIN_ACTION] = 1;
		}
	}

	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was won, the actionRanking for the action is incremented by 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnWin(int[] actionRanking, int action) {

		// Increments actionRanking by 1
		actionRanking[action - Config.MIN_ACTION] += 1;
	}

	/**
	 * Allocates and initializes a 2 dimensional array. The number of rows
	 * corresponds to the number of startSticks. Each row is an actionRanking
	 * with an element for each possible action. The possible actions range from
	 * Config.MIN_ACTION to Config.MAX_ACTION. Each actionRanking is initialized
	 * with the initializeActionRanking method.
	 * 
	 * @param startSticks
	 *            The number of sticks the game is starting with.
	 * @return The two dimensional strategyTable, properly initialized.
	 */
	static int[][] createAndInitializeStrategyTable(int startSticks) {
		// number of rows = startSticks (goes from maximum number of sticks to 1
		// stick)
		// each row is an actionRanking
		// actions go from minimum number of sticks to pick up to max sticks to
		// pick up
		// call initalizeActionRanking method to make every actionRanking = 1

		// Array that initializes the strategyTable
		int[][] strategyTable = new int[startSticks][(Config.MAX_ACTION
				- Config.MIN_ACTION) + 1];

		for (int j = 0; j < strategyTable.length; j++) {

			// Each element in strategyTable is initialized to tempArray
			int[] tempArray = new int[(Config.MAX_ACTION - Config.MIN_ACTION)
					+ 1];

			// Assigns 1 to each element in tempArray
			initializeActionRanking(tempArray);

			strategyTable[j] = tempArray;
		}

		return strategyTable;
	}

	/**
	 * This formats the whole strategyTable as a string utilizing the
	 * actionRankingToString method. For example:
	 * 
	 * Strategy Table Sticks Rankings 10 3,4,11 9 6,2,5 8 7,3,1 etc.
	 * 
	 * The title "Strategy Table" should be proceeded by a \n.
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @return A string containing the properly formatted strategy table.
	 */
	static String strategyTableToString(int[][] strategyTable) {

		String expectedString = "";
		String intro = "\nStrategy Table\n" + "Sticks" + "\t" + "Rankings\n";

		for (int i = strategyTable.length; i > 0; i--) {
			expectedString += actionRankingToString(i, strategyTable[i - 1]);
		}

		return intro + expectedString;
	}

	/**
	 * This updates the strategy table since a game was won.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took
	 * when a given number of sticks remained on the table. Remember that
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, then
	 * the user took 2 sticks when there were 7 sticks remaining on the table.
	 * For each action noted in the history, this calls the
	 * updateActionRankingOnWin method passing the corresponding action and
	 * actionRanking. After calling this method, the actionHistory is cleared
	 * (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * 
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnWin(int[][] strategyTable,
			int[] actionHistory) {
		for (int i = 0; i < actionHistory.length; i++) {
			// System.out.print(actionHistory[i] + " ");
			if (actionHistory[i] > 0) {
				updateActionRankingOnWin(strategyTable[i], actionHistory[i]);
				actionHistory[i] = 0;
			}
		}
	}

	/**
	 * This updates the strategy table for a loss.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took
	 * when a given number of sticks remained on the table. Remember that
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, then
	 * the user took 2 sticks when there were 7 sticks remaining on the table.
	 * For each action noted in the history, this calls the
	 * updateActionRankingOnLoss method passing the corresponding action and
	 * actionRanking. After calling this method, the actionHistory is cleared
	 * (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnLoss(int[][] strategyTable,
			int[] actionHistory) {

		for (int i = 0; i < actionHistory.length; i++) {
			if (actionHistory[i] > 0) {
				updateActionRankingOnLoss(strategyTable[i], actionHistory[i]);
				actionHistory[i] = 0;
			}
		}
	}

	/**
	 * This method simulates a game between two players using their
	 * corresponding strategyTables. Use the aiChooseAction method to choose an
	 * action for each player. Record each player's actions in their
	 * corresponding history array. This method doesn't print out any of the
	 * actions being taken. Player 1 should make the first move in the game.
	 * 
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1StrategyTable
	 *            An array of actionRankings.
	 * @param player1ActionHistory
	 *            An array for recording the actions that occur.
	 * @param player2StrategyTable
	 *            An array of actionRankings.
	 * @param player2ActionHistory
	 *            An array for recording the actions that occur.
	 * @return 1 or 2 indicating which player won the game.
	 */
	static int playAiVsAi(int startSticks, int[][] player1StrategyTable,
			int[] player1ActionHistory, int[][] player2StrategyTable,
			int[] player2ActionHistory) {

		// Stores the number of sticks taken
		int numOfSticksTaken = 0;

		// Variable storing which player gets returned (which player won the
		// game?)
		int player = 0;

		// This boolean variable is true when it's the first player's turn to go
		boolean player1Turn = true;

		while (startSticks > 0) {
			if (player1Turn == true) {
				numOfSticksTaken = aiChooseAction(startSticks,
						player1StrategyTable[startSticks - 1]);
				player1ActionHistory[startSticks - 1] = numOfSticksTaken;
				startSticks = startSticks - numOfSticksTaken;
				player1Turn = false;
			}

			if (player1Turn == false && startSticks > 0) {
				numOfSticksTaken = aiChooseAction(startSticks,
						player2StrategyTable[startSticks - 1]);
				player2ActionHistory[startSticks - 1] = numOfSticksTaken;
				startSticks = startSticks - numOfSticksTaken;
				player1Turn = true;
			}
		}
		if (startSticks == 0 && player1Turn == false) {
			player = 2;
		}

		// Will run if there are no sticks left and it's the user's turn:
		else if (startSticks == 0 && player1Turn == true) {
			player = 1;
		}

		return player;

	}

	/**
	 * This method has the computer play against itself many times. Each time it
	 * plays it records the history of its actions and uses those actions to
	 * improve its strategy.
	 * 
	 * Algorithm: 1) Create a strategy table for each of 2 players with
	 * createAndInitializeStrategyTable. 2) Create an action history for each
	 * player. An action history is a single dimension array of int. Each index
	 * in action history corresponds to the number of sticks remaining where the
	 * 0 index is 1 stick remaining. 3) For each game, 4) Call playAiVsAi with
	 * the return value indicating the winner. 5) Call updateStrategyTableOnWin
	 * for the winner and 6) Call updateStrategyTableOnLoss for the loser. 7)
	 * After the games are played then the strategyTable for whichever strategy
	 * won the most games is returned. When both players win the same number of
	 * games, return the first player's strategy table.
	 * 
	 * @param startSticks
	 *            The number of sticks to start with.
	 * @param numberOfGamesToPlay
	 *            The number of games to play and learn from.
	 * @return A strategyTable that can be used to make action choices when
	 *         playing a person. Returns null if startSticks is less than
	 *         Config.MIN_STICKS or greater than Config.MAX_STICKS. Also returns
	 *         null if numberOfGamesToPlay is less than 1.
	 */
	static int[][] trainAi(int startSticks, int numberOfGamesToPlay) {

		if ((startSticks < Config.MIN_STICKS)
				|| (startSticks > Config.MAX_STICKS)
				|| (numberOfGamesToPlay < 1)) {
			return null;
		}

		// Returns which player won the game
		int winner;

		// Keeps track of how many times each player wins the game
		int player1WinsGame = 0;
		int player2WinsGame = 0;

		int[][] player1StrategyTable = createAndInitializeStrategyTable(
				startSticks);
		int[][] player2StrategyTable = createAndInitializeStrategyTable(
				startSticks);

		int[] player1ActionHistory = new int[startSticks];

		int[] player2ActionHistory = new int[startSticks];

		for (int i = 0; i < numberOfGamesToPlay; i++) {

			winner = playAiVsAi(startSticks, player1StrategyTable,
					player1ActionHistory, player2StrategyTable,
					player2ActionHistory);

			if (winner == 1) {
				updateStrategyTableOnWin(player1StrategyTable,
						player1ActionHistory);
				updateStrategyTableOnLoss(player2StrategyTable,
						player2ActionHistory);
				player1WinsGame++;
			} else if (winner == 2) {
				updateStrategyTableOnWin(player2StrategyTable,
						player2ActionHistory);
				updateStrategyTableOnLoss(player1StrategyTable,
						player1ActionHistory);
				player2WinsGame++;
			}
		}

		if (player1WinsGame >= player2WinsGame) {
			return player1StrategyTable;
		}
		return player2StrategyTable;
	}
}
