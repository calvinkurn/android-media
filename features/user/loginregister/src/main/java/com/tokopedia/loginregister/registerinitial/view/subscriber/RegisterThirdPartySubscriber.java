package com.tokopedia.loginregister.registerinitial.view.subscriber;

import android.content.Context;

import com.tokopedia.loginregister.registerinitial.view.listener.RegisterContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.view.LoginCommonSubscriber;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 10/24/18.
 */
public class RegisterThirdPartySubscriber extends LoginCommonSubscriber<LoginEmailDomain> {
    private static final String CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED";
    private final RegisterContract.View view;
    private final String methodName;

    public RegisterThirdPartySubscriber(Context context,
                                        LoginSuccessRouter router,
                                        String email,
                                        RegisterContract.View view,
                                        String methodName) {
        super(context, router, email);
        this.view = view;
        this.methodName = methodName;
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        super.onNext(loginEmailDomain);

        if (loginEmailDomain.getInfo().getName().contains(CHARACTER_NOT_ALLOWED)) {
            view.onGoToAddName();
        } else if (loginEmailDomain.getLoginResult() != null
                && !isGoToSecurityQuestion(loginEmailDomain.getLoginResult())) {
            view.onSuccessRegisterSosmed(methodName);
        } else {
            view.onErrorRegisterSosmed(methodName, ErrorHandlerSession.getDefaultErrorCodeMessage
                    (ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, view.getContext()));
        }
    }


    @Override
    protected void onErrorLogin(String errorMessage) {
        view.onErrorRegisterSosmed(methodName, errorMessage);
    }

}
