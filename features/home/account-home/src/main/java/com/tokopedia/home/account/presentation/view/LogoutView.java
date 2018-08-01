package com.tokopedia.home.account.presentation.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

public interface LogoutView extends CustomerView {
    void logoutFacebook();

    void onErrorLogout(Throwable throwable);

    void onSuccessLogout();

}
