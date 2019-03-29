package com.tokopedia.loginphone.checkloginphone.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by nisie on 11/23/17.
 */

public interface CheckLoginPhoneNumberContract {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void goToVerifyAccountPage(String phoneNumber);

        void goToNoTokocashAccountPage();

        void dismissLoading();

        void showLoading();

        void onForbidden();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithPhoneNumber(String text);
    }
}
