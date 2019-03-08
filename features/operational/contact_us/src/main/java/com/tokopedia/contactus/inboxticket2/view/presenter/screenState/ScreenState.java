package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

public abstract class ScreenState {


    public int getFirstEmoji() {
        return R.drawable.rating_inactive_1;
    }

    public String getMessageColor() {
        return "#ff4f00";
    }


    public int getSecondEmoji() {
        return R.drawable.rating_inactive_2;
    }


    public int getThirdEmoji() {
        return R.drawable.rating_inactive_3;
    }


    public int getFourthEmoji() {
        return R.drawable.rating_inactive_4;
    }


    public int getFifthEmoji() {
        return R.drawable.rating_inactive_5;
    }


    public String getMessage() {
        return "Tidak Memuaskan";
    }


    public String getQuestion() {
        return "Apa yang bisa diperbaiki dari layanan kami?";
    }

}