/**
 * fevvelasquez 2021, coding exercises about Java JDBC API Basis.
 * 
 * Connect from Java to MySQL database, then execute free DQL and DML Statement(s).
 * Uses: JDBC API, MySQL, SQL / Javadoc, Logger, ResourceBundle.
 */
package me.fevvelasquez.jdbc.api.statement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Statement results generated by {@link SqlExecutor}.
 * 
 * @version 0.2.0. Exercise, exploring 'execute' method from java.sql.Statement.
 * @author fevvelasquez@gmail.com
 */
public class StatementResult {
	private static final Logger logger = Logger.getLogger(StatementResult.class.getName());

	private final Statement statement;
	private final boolean isQuery;
	private final ResultSet resultSet;
	private final int rowsAffected;

	/**
	 * Statement results generated by {@link SqlExecutor}.
	 * 
	 * @param statement    Statement executed.
	 * @param isQuery      Indicates if {@code statement} is a query.
	 * @param resultset    Set of query results.
	 * @param rowsAffected Count of affected rows.
	 */
	StatementResult(Statement statement, boolean isQuery, ResultSet resultSet, int rowsAffected) {
		this.statement = statement;
		this.isQuery = isQuery;
		this.resultSet = resultSet;
		this.rowsAffected = rowsAffected;
	}

	/**
	 * Representation string for the statement result.
	 * 
	 * @return A string representation of a ResultSet if exists, otherwise a string
	 *         representation of affected rows.
	 */
	@Override
	public String toString() {
		try {

			StringBuilder mssg = new StringBuilder();
			if (isQuery) {
				// Header:
				int columnCount = resultSet.getMetaData().getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					mssg.append("[" + resultSet.getMetaData().getColumnLabel(i) + "]");
				}
				// ------------------------------------------------------------------------------------
				// Rows:
				while (resultSet.next()) {
					mssg.append("\n");
					for (int i = 1; i <= columnCount; i++) {
						mssg.append("[" + resultSet.getString(i) + "]");
					}
				}
				// ------------------------------------------------------------------------------------

			} else {
				mssg.append(rowsAffected + " row(s) affected.");
			}
			return mssg.toString();

		} catch (SQLException e) {
			logger.warning(e.getMessage());
		}
		return null;
	}

	/**
	 * Release ResultSet resources if exist.
	 */
	private void resultSet_close() {
		if (isQuery) {
			try {
				resultSet.close();
				logger.info("ResultSet closed.");
			} catch (SQLException e) {
				logger.warning(e.getMessage());
			}
		}
	}

	/**
	 * Release Statement resources.
	 */
	public void close() {
		try {
			resultSet_close();
			statement.close();
			logger.info("Statement closed.");
		} catch (SQLException e) {
			logger.warning(e.getMessage());
		}
	}

	/**
	 * Indicates if the statement is a query.
	 * 
	 * @return {@code true} means a set of results is available through
	 *         {@link getResultset()}.
	 */
	public boolean isQuery() {
		return isQuery;
	}

	/**
	 * Use if statement is a query.
	 * 
	 * @return A set of query results.
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * Use if statement is not a query.
	 * 
	 * @return A count of affected rows.
	 */
	public int getRowsAffected() {
		return rowsAffected;
	}

}
