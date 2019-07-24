package com.tokopedia.csat_rating.presenter.screenState;


import com.tokopedia.csat_rating.R;

public class SecondScreenState extends ScreenState {

    private String mCaption;
    private String mQuestion;

    public SecondScreenState(String mCaption,String question) {
        this.mCaption = mCaption;
        mQuestion = question;
    }

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
        return mCaption;
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }
}
