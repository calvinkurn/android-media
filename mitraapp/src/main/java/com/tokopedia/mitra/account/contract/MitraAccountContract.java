package com.tokopedia.mitra.account.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface MitraAccountContract {
    interface View extends CustomerView {

        void renderName(String name);

        void renderPhoneNumber(String phone);

        void showLogoutConfirmationDialog();

        void navigateToLogin();
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();

        void onLogoutClicked();

        void onLogoutConfirmed();
    }
}
