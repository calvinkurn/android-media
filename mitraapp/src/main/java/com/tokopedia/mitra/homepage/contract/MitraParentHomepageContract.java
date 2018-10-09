package com.tokopedia.mitra.homepage.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface MitraParentHomepageContract {
    interface View extends CustomerView {

        void inflateHomepageFragment();

        void setHomepageMenuSelected();

        void inflateAccountFragment();

        void setAccountMenuSelected();

        void navigateToLoggedInThenAccountPage();

        void showErrorMessageInSnackbar(@StringRes int resId);

        void inflateHelpFragment();

        void setHelpMenuSelected();

        void hideToolbarLogo();

        void setToolbarTitle(@StringRes int resId);

        void showToolbarLogo();

        void hideToolbarTitle();
    }

    interface Presenter extends CustomerPresenter<View> {

        void onHomepageMenuClicked();

        void onAccountMenuClicked();

        void onLoginFromAccountResultReceived();

        void onHelpMenuClicked();

        void onViewCreated();
    }
}
