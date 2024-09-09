package org.userDao;

import org.userRepository.User;

import java.sql.*;

public class UserDao {
    static{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private final String jdbcURL = "jdbc:postgresql://192.168.0.107:5432/postgres";
    private final String jdbcUsername = "postgres";
    private final String jdbcPassword = "root1";
    private static final String INSERT_USER_SQL = "INSERT INTO users (name, surname, age) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT id, name, surname, age FROM users WHERE id = ?;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?;";
    private static final String UPDATE_USER_SQL = "UPDATE users SET name = ?, surname = ?, age = ? WHERE id = ?;";

    protected Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
                return connection;
    }

    public void addUser(User user) throws SQLException{
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.executeUpdate();
        }
    }

    public User selectUser(int id) throws SQLException {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int age = resultSet.getInt("age");
                user = new User(name, surname, age);
            }
        }
        return user;
    }

    public boolean deleteUser(int id) throws SQLException{
        boolean rowDeleted;
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)){
                preparedStatement.setInt(1, id);
                rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException{
        boolean rowUpdated;
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)){
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setInt(4, user.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}
