package com.tokopedia.posapp.auth.login.view.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.auth.login.domain.usecase.PosLoginEmailUseCase;
import com.tokopedia.posapp.auth.login.view.PosLogin;
import com.tokopedia.session.login.loginemail.domain.interactor.LoginEmailUseCase;
import com.tokopedia.session.login.loginemail.domain.model.LoginEmailDomain;
import com.tokopedia.session.login.loginemail.view.subscriber.LoginSubscriber;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author okasurya on 3/6/18.
 */

public class PosLoginPresenter implements PosLogin.Presenter {
    private PosLogin.View view;
    private PosLoginEmailUseCase posLoginEmailUseCase;
    private SessionHandler sessionHandler;

    @Inject
    public PosLoginPresenter(PosLoginEmailUseCase posLoginEmailUseCase,
                             SessionHandler sessionHandler) {
        this.posLoginEmailUseCase = posLoginEmailUseCase;
    }

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
        view.resetError();
        if (isValid(email, password)) {
            view.showLoadingLogin();
            posLoginEmailUseCase.execute(LoginEmailUseCase.getParam(email, password),
                    new LoginSubscriber(view, email));
        }
    }

    @Override
    public void resetToken() {
        sessionHandler.clearToken();
    }

    private boolean isValid(String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(password)) {
            view.showErrorPassword(com.tokopedia.session.R.string.error_field_required);
            isValid = false;
        } else if (password.length() < 4) {
            view.showErrorPassword(com.tokopedia.session.R.string.error_incorrect_password);
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            view.showErrorEmail(com.tokopedia.session.R.string.error_field_required);
            isValid = false;
        } else if (!CommonUtils.EmailValidation(email)) {
            view.showErrorEmail(com.tokopedia.session.R.string.error_invalid_email);
            isValid = false;
        }

        return isValid;
    }
}
