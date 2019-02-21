package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

public abstract class ScreenState {
    int firstEmoji;
    int secondEmoji;
    int thirdEmoji;
    int fourthEmoji;
    int fifthEmoji;
    String message;
    String messageColor;
    String question;

    ScreenState() {
        firstEmoji = R.drawable.rating_inactive_1;
        secondEmoji = R.drawable.rating_inactive_2;
        thirdEmoji = R.drawable.rating_inactive_3;
        fourthEmoji = R.drawable.rating_inactive_4;
        fifthEmoji = R.drawable.rating_inactive_5;
        message = "Tidak Memuaskan";
        messageColor = "#ff4f00";
        question = "Apa yang bisa diperbaiki dari layanan kami?";
        init();
    }
    abstract void init() ;

    public int getFirstEmoji() {
        return firstEmoji;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(String messageColor) {
        this.messageColor = messageColor;
    }

    public void setFirstEmoji(int firstEmoji) {
        this.firstEmoji = firstEmoji;
    }

    public int getSecondEmoji() {
        return secondEmoji;
    }

    public void setSecondEmoji(int secondEmoji) {
        this.secondEmoji = secondEmoji;
    }

    public int getThirdEmoji() {
        return thirdEmoji;
    }

    public void setThirdEmoji(int thirdEmoji) {
        this.thirdEmoji = thirdEmoji;
    }

    public int getFourthEmoji() {
        return fourthEmoji;
    }

    public void setFourthEmoji(int fourthEmoji) {
        this.fourthEmoji = fourthEmoji;
    }

    public int getFifthEmoji() {
        return fifthEmoji;
    }

    public void setFifthEmoji(int fifthEmoji) {
        this.fifthEmoji = fifthEmoji;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}