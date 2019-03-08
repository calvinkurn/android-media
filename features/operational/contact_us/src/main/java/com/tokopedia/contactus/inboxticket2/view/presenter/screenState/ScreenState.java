package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

public abstract class ScreenState {
    protected final int firstEmoji;
    protected final int secondEmoji;
    protected final int thirdEmoji;
    protected final int fourthEmoji;
    protected final int fifthEmoji;
    protected final String message;
    protected final String messageColor;
    protected final String question;

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


    public int getSecondEmoji() {
        return secondEmoji;
    }


    public int getThirdEmoji() {
        return thirdEmoji;
    }


    public int getFourthEmoji() {
        return fourthEmoji;
    }


    public int getFifthEmoji() {
        return fifthEmoji;
    }


    public String getMessage() {
        return message;
    }


    public String getQuestion() {
        return question;
    }

}