package com.tokopedia.loginregister.registeremail.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginregister.registeremail.domain.RegisterEmailUseCase;
import com.tokopedia.loginregister.registeremail.domain.pojo.RegisterEmailPojo;
import com.tokopedia.loginregister.registeremail.view.listener.RegisterEmailContract;
import com.tokopedia.loginregister.registeremail.view.subscriber.RegisterEmailSubscriber;
import com.tokopedia.loginregister.registeremail.view.util.RegisterUtil;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import javax.inject.Inject;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterEmailPresenter extends BaseDaggerPresenter<RegisterEmailContract.View> implements RegisterEmailContract.Presenter {

    private RegisterEmailUseCase registerEmailUseCase;

    @Inject
    public RegisterEmailPresenter(RegisterEmailUseCase registerEmailUseCase) {
        this.registerEmailUseCase = registerEmailUseCase;
    }

    @Override
    public void onRegisterClicked(String email, String name, String password, String confirmPassword, int isAutoVerify) {
        getView().resetError();
        getView().showLoadingProgress();
        getView().dropKeyboard();
        registerEmailUseCase.execute(
                RegisterEmailUseCase.getParam(
                        email,
                        name,
                        password,
                        confirmPassword,
                        isAutoVerify
                ),
                new RegisterEmailSubscriber(getView(),
                        email, name, password));

    }

    @Override
    public boolean isCanRegister(String name, String email, String password) {
        boolean isValid = true;

        int PASSWORD_MINIMUM_LENGTH = 6;
        if (TextUtils.isEmpty(password)) {
            isValid = false;
        } else if (password.length() < PASSWORD_MINIMUM_LENGTH) {
            isValid = false;
        }

        if (TextUtils.isEmpty(name)) {
            isValid = false;
        } else if (RegisterUtil.checkRegexNameLocal(name)) {
            isValid = false;
        } else if (RegisterUtil.isBelowMinChar(name)) {
            isValid = false;
        } else if (RegisterUtil.isExceedMaxCharacter(name)) {
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void detachView() {
        super.detachView();
        registerEmailUseCase.unsubscribe();
    }
}
