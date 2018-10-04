package com.tokopedia.home.account.presentation.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * @author hadi putra
 */
public interface LogoutView extends CustomerView {
    void logoutFacebook();

    void onErrorLogout(Throwable throwable);

    void onSuccessLogout();

}
