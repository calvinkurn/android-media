package com.tokopedia.linker.model;

public class UserData {

    private String email;
    private String phoneNumber;
    private boolean loggedin;
    private boolean isFirstTimeUser;
    private String userId;
    private String name;
    private String medium;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public boolean isFirstTimeUser() {
        return isFirstTimeUser;
    }

    public void setFirstTimeUser(boolean firstTimeUser) {
        isFirstTimeUser = firstTimeUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
