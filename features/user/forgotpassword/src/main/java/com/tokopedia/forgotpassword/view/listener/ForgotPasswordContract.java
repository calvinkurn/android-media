package com.tokopedia.forgotpassword.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by nisie on 9/25/18.
 */

public interface ForgotPasswordContract {

    interface View extends CustomerView {
        Context getContext();

        void resetError();

        void showLoadingProgress();

        void onErrorResetPassword(String errorMessage);

        void onSuccessResetPassword();

        void setEmailError(String error);
    }

    interface Presenter extends CustomerPresenter<View> {

        void resetPassword(String email);

    }
}