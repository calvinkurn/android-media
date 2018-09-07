package com.tokopedia.mitra.homepage.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface MitraHomepageContract {
    interface View extends CustomerView {

        void showLoginContainer();

        void hideLoginContainer();

        void showMessageInRedSnackBar(@StringRes int resId);

        void navigateToLoginPage();
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onLoginResultReceived();

        void onLoginBtnClicked();
    }
}
