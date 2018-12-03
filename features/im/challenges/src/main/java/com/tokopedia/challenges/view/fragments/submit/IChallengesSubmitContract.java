package com.tokopedia.challenges.view.fragments.submit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;

public interface IChallengesSubmitContract {

    interface View extends CustomerView {
        String getDescription();

        Context getContext();

        void setDescriptionError(String error);

        String getImageTitle();

        String getImage();

        void showProgress(String message);

        void hideProgress();

        void showMessage(String showToast);

        void finish();

        void setSnackBarErrorMessage(String message);

        void setChallengeData();

        void selectImageVideo();

        void selectImage();

        void selectVideo();

        String getChallengeId();

        ChallengeSettings getChallengeSettings();

        Activity getActivity();

        void setSubmitButtonText(String text);

        void setChooseImageText(String text);

        void saveLocalpath(String newPostId, String filePath);

        void sendBroadcast(Intent intent1);

        String getChallengeTitle();
    }

    interface Presenter extends CustomerPresenter<View> {
        void onSubmitButtonClick();

        void onCancelButtonClick();

        void onSelectedImageClick();

        void setSubmitButtonText();

        void onDestroy();
    }
}
