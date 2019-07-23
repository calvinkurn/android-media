package com.tokopedia.csat_rating.presenter.screenState;


import com.tokopedia.csat_rating.R;

public class SecondScreenState extends ScreenState {

    @Override
    public int getFirstEmoji() {
        return getSecondEmoji();

    }
    @Override
    public int getSecondEmoji() {
        return R.drawable.rating_active_2;
    }

    @Override
    public String getMessage() {
        return "Kurang Memuaskan";
    }
}
