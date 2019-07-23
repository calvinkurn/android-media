package com.tokopedia.csat_rating.presenter;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.csat_rating.ProvideRatingContract;
import com.tokopedia.csat_rating.presenter.screenState.FifthScreenState;
import com.tokopedia.csat_rating.presenter.screenState.FirstScreenState;
import com.tokopedia.csat_rating.presenter.screenState.FourthScreenState;
import com.tokopedia.csat_rating.presenter.screenState.ScreenState;
import com.tokopedia.csat_rating.presenter.screenState.SecondScreenState;
import com.tokopedia.csat_rating.presenter.screenState.ThirdScreenState;

public class BaseProvideRatingFragmentPresenter extends BaseDaggerPresenter<ProvideRatingContract.ProvideRatingView>  implements ProvideRatingContract.ProvideRatingPresenter {

    public static final String EMOJI_STATE = "emoji_state";
    public static final String SELECTED_ITEM = "selected_items";

    private int emojiState = 0;

    public BaseProvideRatingFragmentPresenter() {
    }

    @Override
    public void attachView(ProvideRatingContract.ProvideRatingView view) {
        super.attachView(view);
        emojiState = getView().getSelectedEmoji();
        updateScreenState();
        getView().setFilterList(getView().getReasonList());
    }

    public ScreenState getScreenState(int emoji) {
        ScreenState screenState = null;
        switch (emoji) {
            case 1:
                screenState = new FirstScreenState();
                break;
            case 2:
                screenState = new SecondScreenState();
                break;
            case 3:
                screenState = new ThirdScreenState();
                break;
            case 4:
                screenState = new FourthScreenState();
                break;
            case 5:
                screenState = new FifthScreenState();
                break;

        }
        return screenState;

    }

    public void updateScreenState() {
        getView().clearEmoji();
        ScreenState screenState = getScreenState(emojiState);
        getView().setFirstEmoji(screenState.getFirstEmoji());
        getView().setSecondEmoji(screenState.getSecondEmoji());
        getView().setThirdEmoji(screenState.getThirdEmoji());
        getView().setFourthEmoji(screenState.getFourthEmoji());
        getView().setFifthEmoji(screenState.getFifthEmoji());
        getView().setMessage(screenState.getMessage());
        getView().setMessageColor(screenState.getMessageColor());
        getView().setQuestion(screenState.getQuestion());
    }


    @Override
    public void onFirstEmojiClick() {
        emojiState = 1;
        updateScreenState();

    }

    @Override
    public void onSecondEmojiClick() {
        emojiState = 2;
        updateScreenState();
    }

    @Override
    public void onThirdEmojiClick() {
        emojiState = 3;
        updateScreenState();
    }

    @Override
    public void onFourthEmojiClick() {
        emojiState = 4;
        updateScreenState();
    }

    @Override
    public void onFifthEmojiClick() {
        emojiState = 5;
        updateScreenState();
    }

    @Override
    public void onSubmitClick() {
        Intent intent = new Intent();
        intent.putExtra(EMOJI_STATE,emojiState);
        intent.putExtra(SELECTED_ITEM,getView().getSelectedItem());
        getView().onSuccessSubmit(intent);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
