package com.tokopedia.posapp.auth.validatepassword.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.auth.validatepassword.domain.ValidatePasswordUseCase;
import com.tokopedia.posapp.auth.validatepassword.view.ValidatePassword;
import com.tokopedia.posapp.auth.validatepassword.view.CheckPasswordSubscriber;

import javax.inject.Inject;

/**
 * Created by okasurya on 9/27/17.
 */

public class ValidatePasswordPresenter implements ValidatePassword.Presenter {
    private ValidatePasswordUseCase validatePasswordUseCase;
    private ValidatePassword.View viewListener;

    @Inject
    public ValidatePasswordPresenter(ValidatePasswordUseCase validatePasswordUseCase) {
        this.validatePasswordUseCase = validatePasswordUseCase;
    }

    @Override
    public void checkPassword(final String password) {
        RequestParams params = RequestParams.create();
        params.putString("password", password);

        validatePasswordUseCase.execute(params, new CheckPasswordSubscriber(viewListener));
    }

    public void attachView(ValidatePassword.View viewListener) {
        this.viewListener = viewListener;
    }
}
