package com.tokopedia.csat_rating.presenter.screenState;


import com.tokopedia.csat_rating.R;

public class ThirdScreenState extends ScreenState {

    private String mCaption;
    private String mQuestion;

    public ThirdScreenState(String mCaption, String mQuestion) {
        this.mCaption = mCaption;
        this.mQuestion = mQuestion;
    }

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
        return mCaption;
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }

    @Override
    public String getMessageColor() {
        return "#fec100";
    }
}
