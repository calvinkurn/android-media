package com.tokopedia.csat_rating;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.csat_rating.data.BadCsatReasonListItem;

import java.util.ArrayList;
import java.util.List;

public interface ProvideRatingContract {
    public interface ProvideRatingView extends CustomerView {
        public void setFirstEmoji(int drawable);
        public void setSecondEmoji(int drawable);
        public void setThirdEmoji(int drawable);
        public void setFourthEmoji(int drawable);
        public void setFifthEmoji(int drawable);
        public void setMessage(String message);
        public void setMessageColor(String color);
        public void setQuestion(String question);
        public int getSelectedEmoji();
        public void clearEmoji();
        public void showErrorMessage(String o);
        public void setFilterList(List<BadCsatReasonListItem> filterList);
        public String getSelectedItem();
        List<BadCsatReasonListItem> getReasonList();
        void onSuccessSubmit(Intent intent);
        public ArrayList<String> getcaption();
        public ArrayList<String> getQuestion();


        void showProgress();

        void hideProgress();

    }
    public interface ProvideRatingPresenter extends CustomerPresenter<ProvideRatingView> {

        public void onFirstEmojiClick();
        public void onSecondEmojiClick();
        public void onThirdEmojiClick();
        public void onFourthEmojiClick();
        public void onFifthEmojiClick();
        void onSubmitClick();

    }
}
