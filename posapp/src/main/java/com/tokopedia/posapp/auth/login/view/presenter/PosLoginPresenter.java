package com.tokopedia.posapp.auth.login.view.presenter;

import com.tokopedia.posapp.auth.login.view.PosLogin;

/**
 * @author okasurya on 3/6/18.
 */

public class PosLoginPresenter implements PosLogin.Presenter {
    private PosLogin.View view;

    @Override
    public void attachView(PosLogin.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void login(String email, String password) {

    }
}
