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

import java.util.ArrayList;

public class BaseProvideRatingFragmentPresenter extends BaseDaggerPresenter<ProvideRatingContract.ProvideRatingView>  implements ProvideRatingContract.ProvideRatingPresenter {

    public static final String EMOJI_STATE = "emoji_state";
    public static final String SELECTED_ITEM = "selected_items";
    public static final int FIRST_EMOGI = 1;
    public static final int SECOND_EMOGI = 2;
    public static final int THIRD_EMOGI = 3;
    public static final int FOURTH_EMOGI = 4;
    public static final int FIFTH_EMOGI = 5;
    private ArrayList<String> captionsList;
    private ArrayList<String> questionList;

    protected int emojiState = 0;

    public BaseProvideRatingFragmentPresenter() {
    }

    @Override
    public void attachView(ProvideRatingContract.ProvideRatingView view) {
        super.attachView(view);
        captionsList = getView().getcaption();
        questionList = getView().getQuestion();
        emojiState = getView().getSelectedEmoji();
        updateScreenState();
        getView().setFilterList(getView().getReasonList());
    }

    public ScreenState getScreenState(int emoji) {
        ScreenState screenState = null;
        switch (emoji) {
            case FIRST_EMOGI:
                screenState = new FirstScreenState(captionsList.get(FIRST_EMOGI-1),questionList.get(FIRST_EMOGI-1));
                break;
            case SECOND_EMOGI:
                screenState = new SecondScreenState(captionsList.get(SECOND_EMOGI-1),questionList.get(SECOND_EMOGI-1));
                break;
            case THIRD_EMOGI:
                screenState = new ThirdScreenState(captionsList.get(THIRD_EMOGI-1),questionList.get(THIRD_EMOGI-1));
                break;
            case FOURTH_EMOGI:
                screenState = new FourthScreenState(captionsList.get(FOURTH_EMOGI-1),questionList.get(FOURTH_EMOGI-1));
                break;
            case FIFTH_EMOGI:
                screenState = new FifthScreenState(captionsList.get(FIFTH_EMOGI-1),questionList.get(FIFTH_EMOGI-1));
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
