package com.tokopedia.loginregister.registeremail.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.loginregister.registeremail.domain.pojo.RegisterEmailPojo;

/**
 * @author by nisie on 10/25/18.
 */
public interface RegisterEmailContract {

    interface View extends CustomerView {

        void resetError();

        String getString(int resId);

        void setActionsEnabled(boolean isEnabled);

        void showLoadingProgress();

        void dismissLoadingProgress();

        void goToActivationPage(RegisterEmailPojo pojo, String email, String password);

        void goToAutomaticLogin();

        void dropKeyboard();

        void onErrorRegister(String errorMessage);

        void onSuccessRegister(RegisterEmailPojo registerEmailPojo, String name, String email, String phone);

        void showInfo();

        void onForbidden();

        Context getContext();

        void onBackPressed();
    }

    interface Presenter extends CustomerPresenter<View> {
        void onRegisterClicked(String email, String name, String password, String
                confirmPassword, String phone, int isAutoVerify);

        boolean isCanRegister(String name, String email, String password, String phone);
    }
}