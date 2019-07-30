package com.tokopedia.loginregister.registeremail.view.subscriber;

import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.registeremail.domain.pojo.RegisterEmailPojo;
import com.tokopedia.loginregister.registeremail.view.listener.RegisterEmailContract;
import com.tokopedia.otp.common.network.ErrorMessageException;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import rx.Subscriber;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterEmailSubscriber extends Subscriber<RegisterEmailPojo> {
    private static final String ALREADY_REGISTERED = "sudah terdaftar";

    private static final int GO_TO_REGISTER = 0;
    private static final int GO_TO_ACTIVATION_PAGE = 1;
    private static final int GO_TO_LOGIN = 2;
    private static final int GO_TO_RESET_PASSWORD = 3;

    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_PENDING = -1;
    private static final int STATUS_INACTIVE = 0;

    private final RegisterEmailContract.View viewListener;
    private final String email;
    private final String name;
    private final String password;

    public RegisterEmailSubscriber(RegisterEmailContract.View viewListener,
                                   String email, String name, String password) {
        this.viewListener = viewListener;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissLoadingProgress();
        if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null
                && e.getLocalizedMessage().contains(ALREADY_REGISTERED)) {
            viewListener.showInfo();
        } else  if (e instanceof ErrorMessageException
                && e.getLocalizedMessage() != null) {
            viewListener.onErrorRegister(e.getLocalizedMessage());
        }else {
            ErrorHandlerSession.getErrorMessage(new ErrorHandlerSession.ErrorForbiddenListener() {
                @Override
                public void onForbidden() {
                    viewListener.onForbidden();
                }

                @Override
                public void onError(String errorMessage) {
                    viewListener.onErrorRegister(errorMessage);
                }
            }, e, viewListener.getContext());
        }
    }

    @Override
    public void onNext(RegisterEmailPojo registerEmailPojo) {
        if (registerEmailPojo.getIsSuccess() == 1) {
            viewListener.onSuccessRegister(registerEmailPojo, name, email);
            checkSmartRegister(registerEmailPojo);
        } else {
            viewListener.onErrorRegister(
                    viewListener.getString(R.string.default_request_error_unknown));
        }
    }

    private void checkSmartRegister(RegisterEmailPojo pojo) {
        switch (pojo.getAction()) {
            case GO_TO_LOGIN:
                viewListener.goToAutomaticLogin();
                break;
            case GO_TO_REGISTER:
            case GO_TO_ACTIVATION_PAGE:
                if (pojo.getIsActive() == STATUS_ACTIVE)
                    viewListener.goToAutomaticLogin();
                else if (pojo.getIsActive() == STATUS_INACTIVE || pojo.getIsActive() == STATUS_PENDING)
                    viewListener.goToActivationPage(pojo, email, password);
                break;
            case GO_TO_RESET_PASSWORD:
                viewListener.showInfo();
                break;
            default:
                viewListener.onErrorRegister(ErrorHandlerSession.getDefaultErrorCodeMessage(
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, viewListener.getContext()));
        }
    }
}
