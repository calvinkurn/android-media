package com.tokopedia.posapp.auth.login.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author okasurya on 3/6/18.
 */

public interface PosLogin {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

        void login(String email, String password);
    }
}
