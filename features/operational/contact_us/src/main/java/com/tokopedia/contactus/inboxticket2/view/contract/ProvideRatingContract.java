package com.tokopedia.contactus.inboxticket2.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.contactus.inboxticket2.data.model.BadCsatReasonListItem;

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
        public String getCommentId();
        List<BadCsatReasonListItem> getReasonList();
        void onSuccessSubmit();

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
