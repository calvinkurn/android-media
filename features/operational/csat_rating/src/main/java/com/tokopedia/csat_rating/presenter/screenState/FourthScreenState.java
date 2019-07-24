package com.tokopedia.csat_rating.presenter.screenState;

import com.tokopedia.csat_rating.R;

public class FourthScreenState extends ScreenState {

    private String mCaption;
    private String mQuestion;

    public FourthScreenState(String mCaption, String mQuestion) {
        this.mCaption = mCaption;
        this.mQuestion = mQuestion;
    }

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
        return mCaption;
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }
}
