package com.tokopedia.loginphone.checkregisterphone.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 26/2/18.
 */

public interface CheckRegisterPhoneNumber {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void goToVerifyAccountPage(String phoneNumber);

        void goToLoginPhoneNumber();

        void showConfirmationPhoneNumber(String phoneNumber);

        void dismissLoading();

        void showLoading();

        void showAlreadyRegisteredDialog(String phoneNumber);

        Context getContext();

    }

    interface Presenter extends CustomerPresenter<View> {

        void checkPhoneNumber(String phoneNumber);
    }
}
