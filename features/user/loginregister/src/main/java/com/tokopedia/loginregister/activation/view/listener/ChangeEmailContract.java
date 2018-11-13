package com.tokopedia.loginregister.activation.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by nisie on 10/19/18.
 */
public interface ChangeEmailContract {

    interface View extends CustomerView {

        void showLoadingProgress();

        Context getContext();

        void onErrorChangeEmail(String errorMessage);

        void onSuccessChangeEmail();

        void setPasswordError(String errorMessage);

        void setNewEmailError(String errorMessage);

        void setOldEmailError(String errorMessage);
    }

    interface Presenter extends CustomerPresenter<View> {

        void changeEmail(String oldEmail, String newEmail, String password);
    }
}
