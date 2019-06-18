package com.tokopedia.useridentification.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

/**
 * @author by alvinatin on 21/11/18.
 */

public interface UserIdentificationUploadImage  {
    interface View extends CustomerView {
        Context getContext();

        void goToNextActivity();

        void onSuccessUpload();

        void onErrorUpload(String error);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void uploadImage(UserIdentificationStepperModel model,int projectid);
    }
}
