package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

public class FourthScreenState extends ScreenState {
    @Override
    public int getFirstEmoji() {
        return getFourthEmoji();
    }

    @Override
    public int getSecondEmoji() {
        return getFourthEmoji();
    }

    @Override
    public int getThirdEmoji() {
        return getFourthEmoji();
    }

    @Override
    public int getFourthEmoji() {
        return R.drawable.rating_active_4;
    }

    @Override
    public String getMessageColor() {
        return "#00ac47";
    }

    @Override
    public String getMessage() {
        return "Memuaskan";
    }

    @Override
    public String getQuestion() {
        return "Apa yang bisa ditingkatkan dari layanan kami?";
    }
}
