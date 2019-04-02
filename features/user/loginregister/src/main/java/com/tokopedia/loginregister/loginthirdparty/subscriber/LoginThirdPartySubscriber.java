package com.tokopedia.loginregister.loginthirdparty.subscriber;

import android.content.Context;

import com.tokopedia.loginregister.login.view.listener.LoginContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.view.LoginCommonSubscriber;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 10/19/18.
 */
public class LoginThirdPartySubscriber extends LoginCommonSubscriber<LoginEmailDomain> {
    private static final String CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED";
    private final LoginContract.View view;
    private final String loginMethodName;

    public LoginThirdPartySubscriber(Context context,
                                     LoginSuccessRouter router,
                                     String email,
                                     LoginContract.View view,
                                     String loginMethodName) {
        super(context, router, email);
        this.view = view;
        this.loginMethodName = loginMethodName;
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        super.onNext(loginEmailDomain);

        if (!isGoToSecurityQuestion(loginEmailDomain.getLoginResult())
                && !isGoToCreatePassword(loginEmailDomain)
                && !isGoToPhoneVerification(loginEmailDomain)) {

            if (loginEmailDomain.getInfo().getName().contains(CHARACTER_NOT_ALLOWED)) {
                view.onGoToAddName();
            } else if (loginEmailDomain.getLoginResult() != null
                    && !isGoToSecurityQuestion(loginEmailDomain.getLoginResult())) {
                view.onSuccessLoginSosmed(loginMethodName);
            } else {
                view.dismissLoadingLogin();
                view.onErrorLoginSosmed(loginMethodName, ErrorHandlerSession.getDefaultErrorCodeMessage
                        (ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, view.getContext()));
                router.logUnknownError(new Throwable("Login Result is null"));
            }

        }
    }

    @Override
    protected void onErrorLogin(String errorMessage) {
        view.onErrorLoginSosmed(loginMethodName, errorMessage);
    }

    @Override
    protected boolean isGoToPhoneVerification(LoginEmailDomain loginEmailDomain) {
        return super.isGoToPhoneVerification(loginEmailDomain)
                && view.isFromRegister();
    }
}
