package com.tokopedia.loginregister.registerinitial.view.subscriber;

import android.content.Context;

import com.tokopedia.loginregister.registerinitial.view.listener.RegisterInitialContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.view.LoginCommonSubscriber;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 10/24/18.
 */
public class RegisterThirdPartySubscriber extends LoginCommonSubscriber<LoginEmailDomain> {
    private static final String CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED";
    private final RegisterInitialContract.View view;
    private final String methodName;

    public RegisterThirdPartySubscriber(Context context,
                                        String email,
                                        RegisterInitialContract.View view,
                                        String methodName) {
        super(context, null, email);
        this.view = view;
        this.methodName = methodName;
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        super.onNext(loginEmailDomain);

        //TODO DELETE
        if (!isGoToSecurityQuestion(loginEmailDomain.getLoginResult())
                && !isGoToCreatePassword(loginEmailDomain)
                && !isGoToPhoneVerification(loginEmailDomain)) {

            if (loginEmailDomain.getInfo().getName().contains(CHARACTER_NOT_ALLOWED)) {
                view.onGoToAddName();
            } else if (loginEmailDomain.getLoginResult() != null
                    && !isGoToSecurityQuestion(loginEmailDomain.getLoginResult())) {
                view.onSuccessRegisterSosmed(methodName);
            } else {
//                view.onErrorRegisterSosmed(methodName, ErrorHandlerSession.getDefaultErrorCodeMessage
//                        (ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW, view.getContext()));
               // router.logUnknownError(new Throwable("Login Result is null"));
            }
        }

    }


    @Override
    protected void onErrorLogin(String errorMessage) {
        view.onErrorRegisterSosmed(methodName, errorMessage);
    }
}
