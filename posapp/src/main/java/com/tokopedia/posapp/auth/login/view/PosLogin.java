package com.tokopedia.posapp.auth.login.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;

/**
 * @author okasurya on 3/6/18.
 */

public interface PosLogin {
    interface View extends Login.View {

    }

    interface Presenter extends CustomerPresenter<View> {

        void login(String email, String password);

        void resetToken();
    }
}
