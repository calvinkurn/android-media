package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

public class ThirdScreenState extends ScreenState {


    @Override
    public int getFirstEmoji() {
        return getThirdEmoji();
    }

    @Override
    public int getSecondEmoji() {
        return getThirdEmoji();
    }

    @Override
    public int getThirdEmoji() {
        return R.drawable.rating_active_3;
    }

    @Override
    public String getMessage() {
        return "Cukup Memuaskan";
    }

    @Override
    public String getQuestion() {
        return "Apa yang bisa ditingkatkan dari layanan kami?";
    }

    @Override
    public String getMessageColor() {
        return "#fec100";
    }
}
