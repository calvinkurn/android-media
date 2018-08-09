package com.tokopedia.challenges.view.fragments.submit;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface IChallengesSubmitContract {

    public interface View extends CustomerView {
        public String getDescription();
        public Context getContext();
        public void setDescriptionError(String error);
        public String getImageTitle();
        public String getImage();
        public void showProgress(String message);
        public void hideProgress();
        public void showMessage(String showToast);
        public void finish();
        void setSnackBarErrorMessage(String message);

    }

    public interface Presenter extends CustomerPresenter<View> {
        void onSubmitButtonClick();
        void onCancelButtonClick();
    }
}
