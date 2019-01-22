package com.tokopedia.core.session.model;


/**
 * Created by m.normansyah on 09/11/2015.
 */

public class SecurityQuestionViewModel {
    int security1;
    int security2;
    String email;
    String userID;
    String question;
    String vAnswer;
    String vInputOtp;
    String phone;
    boolean isErrorDisplay;
    boolean isLoading;
    public static final int IS_SECURITY_LOADING_TYPE = 0;
    public static final int IS_ERROR_SHOWN = 1;
    public static final int SEC_2 = 2;

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public int getSecurity1() {
        return security1;
    }

    public void setSecurity1(int security1) {
        this.security1 = security1;
    }

    public int getSecurity2() {
        return security2;
    }

    public void setSecurity2(int security2) {
        this.security2 = security2;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getvAnswer() {
        return vAnswer;
    }

    public void setvAnswer(String vAnswer) {
        this.vAnswer = vAnswer;
    }

    public String getvInputOtp() {
        return vInputOtp;
    }

    public void setvInputOtp(String vInputOtp) {
        this.vInputOtp = vInputOtp;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isErrorDisplay() {
        return isErrorDisplay;
    }

    public void setIsErrorDisplay(boolean isErrorDisplay) {
        this.isErrorDisplay = isErrorDisplay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SecurityQuestionViewModel{" +
                "security1=" + security1 +
                ", security2=" + security2 +
                ", userID='" + userID + '\'' +
                ", question='" + question + '\'' +
                ", vAnswer='" + vAnswer + '\'' +
                ", vInputOtp='" + vInputOtp + '\'' +
                ", isErrorDisplay=" + isErrorDisplay +
                '}';
    }
}
