package com.tokopedia.forgotpassword.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.forgotpassword.R;
import com.tokopedia.forgotpassword.data.pojo.ResetPasswordPojo;
import com.tokopedia.forgotpassword.domain.ResetPasswordUseCase;
import com.tokopedia.forgotpassword.view.listener.ForgotPasswordContract;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 9/25/18.
 */
public class ForgotPasswordPresenter extends BaseDaggerPresenter<ForgotPasswordContract.View>
        implements ForgotPasswordContract.Presenter {

    private final ResetPasswordUseCase resetPasswordUseCase;

    @Inject
    public ForgotPasswordPresenter(ResetPasswordUseCase resetPasswordUseCase) {
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @Override
    public void resetPassword(String email) {
        getView().resetError();

        if (isValidForm(email)) {
            getView().showLoadingProgress();
            resetPasswordUseCase.execute(ResetPasswordUseCase.getParam(email),
                    new Subscriber<ResetPasswordPojo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().onErrorResetPassword(ErrorHandler.getErrorMessage(getView()
                                    .getContext(), e));
                        }

                        @Override
                        public void onNext(ResetPasswordPojo resetPasswordPojo) {
                            if (resetPasswordPojo.getIsSuccess() == 1) {
                                getView().onSuccessResetPassword();
                            } else {
                                getView().onErrorResetPassword("");
                            }
                        }
                    });
        }
    }

    private boolean isValidForm(String email) {
        Boolean isValid = true;

        if (email.length() == 0) {
            getView().setEmailError(getView().getContext().getString(R.string
                    .error_field_required));
            isValid = false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            getView().setEmailError(getView().getContext().getString(R.string
                    .error_invalid_email));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void detachView() {
        super.detachView();
        resetPasswordUseCase.unsubscribe();
    }
}

