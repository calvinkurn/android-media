package com.tokopedia.loginphone.choosetokocashaccount.view.subscriber;

import android.content.Context;

import com.tokopedia.loginphone.choosetokocashaccount.data.LoginTokoCashViewModel;
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.view.LoginCommonSubscriber;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 29/10/18.
 */
public class LoginPhoneNumberSubscriber extends LoginCommonSubscriber<LoginTokoCashViewModel> {

    private final ChooseTokocashAccountContract.View view;

    public LoginPhoneNumberSubscriber(Context context,
                                      LoginSuccessRouter router,
                                      String email,
                                      ChooseTokocashAccountContract.View view) {
        super(context, router, email);
        this.view = view;
    }


    @Override
    public void onNext(LoginTokoCashViewModel loginEmailDomain) {
        super.onNext(loginEmailDomain);

        if (loginEmailDomain.getLoginResult().getIsLogin().equals("1")) {
            view.onSuccessLogin();
        } else {
            router.onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage
                    (ErrorHandlerSession.ErrorCode
                            .UNSUPPORTED_FLOW, view.getContext()));
        }

    }

    @Override
    protected boolean isGoToCreatePassword(LoginEmailDomain loginEmailDomain) {
        return false;
    }

    @Override
    protected boolean isGoToPhoneVerification(LoginEmailDomain loginEmailDomain) {
        return false;
    }
}
