package com.tokopedia.loginregister.login.view.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.loginregister.login.view.listener.LoginContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.view.LoginCommonSubscriber;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 10/16/18.
 */
public class LoginSubscriber extends LoginCommonSubscriber<LoginEmailDomain> {

    private final LoginContract.View view;

    public LoginSubscriber(Context context,
                           LoginSuccessRouter router,
                           String email,
                           LoginContract.View view) {
        super(context, router, email);
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.trackErrorLoginEmail();
        view.stopTrace();
        view.enableArrow();
        super.onError(e);
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        view.stopTrace();
        super.onNext(loginEmailDomain);

        if (!isGoToSecurityQuestion(loginEmailDomain.getLoginResult())
                && !isGoToCreatePassword(loginEmailDomain)
                && !isGoToPhoneVerification(loginEmailDomain)) {

            if (loginEmailDomain.getLoginResult() != null) {
                view.setSmartLock();
                view.onSuccessLoginEmail();
            } else {
                view.onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode
                        .UNSUPPORTED_FLOW, context));
                router.logUnknownError(new Throwable("Login Result is null"));

            }

        }
    }

    @Override
    protected void goToPhoneVerification() {
        view.setSmartLock();
        super.goToPhoneVerification();
    }

    @Override
    protected void goToSecurityQuestion(LoginEmailDomain loginEmailDomain) {
        view.setSmartLock();
        super.goToSecurityQuestion(loginEmailDomain);
    }

    /**
     * @param loginEmailDomain
     * @return true if user login from register page. Otherwise skip phone verif page.
     * return true if not sellerapp because sellerapp does not need phone verif.
     */
    @Override
    protected boolean isGoToPhoneVerification(LoginEmailDomain loginEmailDomain) {
        return super.isGoToPhoneVerification(loginEmailDomain)
                && view.isFromRegister()
                && !GlobalConfig.isSellerApp();
    }

    @Override
    protected void onErrorLogin(String errorMessage) {
        view.onErrorLogin(errorMessage);
    }
}
