package com.tokopedia.csat_rating.presenter.screenState;

import com.tokopedia.csat_rating.R;

public class FirstScreenState extends ScreenState {

    private String mCaption;
    private String mQuestion;

    public FirstScreenState(String caption,String question) {
        mCaption = caption;
        mQuestion = question;
    }


    @Override
    public int getFirstEmoji() {
        return R.drawable.rating_active_1;
    }

    @Override
    public String getMessage() {
        return mCaption;
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }
}
