package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

public class FifthScreenState extends ScreenState {

    @Override
    public int getFirstEmoji() {
        return getFifthEmoji();
    }

    @Override
    public int getSecondEmoji() {
        return getFifthEmoji();
    }

    @Override
    public int getThirdEmoji() {
        return getFifthEmoji();
    }

    @Override
    public int getFourthEmoji() {
        return getFifthEmoji();
    }

    @Override
    public int getFifthEmoji() {
        return  R.drawable.rating_active_5;
    }

    @Override
    public String getMessageColor() {
        return "#00ac47";
    }

    @Override
    public String getMessage() {
        return "Sangat Memuaskan";
    }

    @Override
    public String getQuestion() {
        return "Apa yang sebaiknya dipertahankan dari layanan kami?";
    }
}
