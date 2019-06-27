package com.tokopedia.contactus.inboxticket2.view.presenter.screenState;

import com.tokopedia.contactus.R;

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
