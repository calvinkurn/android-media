package com.tokopedia.loginregister.activation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginregister.activation.domain.pojo.ActionPojo;
import com.tokopedia.loginregister.activation.domain.usecase.ChangeEmailUseCase;
import com.tokopedia.loginregister.activation.view.listener.ChangeEmailContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.loginregister.R;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 10/19/18.
 */
public class ChangeEmailPresenter extends BaseDaggerPresenter<ChangeEmailContract.View> implements
        ChangeEmailContract.Presenter {

    private final ChangeEmailUseCase changeEmailUseCase;

    @Inject
    public ChangeEmailPresenter(ChangeEmailUseCase changeEmailUseCase) {
        this.changeEmailUseCase = changeEmailUseCase;
    }


    @Override
    public void changeEmail(String oldEmail, String newEmail, String password) {
        if (isValid(oldEmail, newEmail, password)) {
            getView().showLoadingProgress();
            changeEmailUseCase.execute(ChangeEmailUseCase.getParam(
                    oldEmail, newEmail, password
            ), new Subscriber<ActionPojo>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if(getView().getContext() != null){
                        getView().onErrorChangeEmail(ErrorHandlerSession.getErrorMessage(getView()
                                .getContext(), e));
                    }
                }

                @Override
                public void onNext(ActionPojo actionPojo) {
                    if (actionPojo.getIsSuccess() == 1) {
                        getView().onSuccessChangeEmail();
                    } else {
                        getView().onErrorChangeEmail(ErrorHandlerSession.getDefaultErrorCodeMessage(
                                ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW,
                                getView().getContext()
                        ));
                    }
                }
            });
        }
    }

    private boolean isValid(String oldEmail, String newEmail, String password) {
        boolean isValid = true;

        if (password.length() == 0) {
            getView().setPasswordError(getView().getContext().getString(R.string.error_field_required));
            isValid = false;
        }

        if (newEmail.length() == 0) {
            getView().setNewEmailError(getView().getContext().getString(R.string
                    .error_field_required));
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            getView().setNewEmailError(getView().getContext().getString(R.string
                    .error_invalid_email));
            isValid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(oldEmail).matches()) {
            getView().setOldEmailError(getView().getContext().getString(R.string
                    .error_invalid_email));
            isValid = false;
        }

        return isValid;
    }
}
