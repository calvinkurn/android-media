package com.tokopedia.loginregister.login.view.subscriber;

import android.content.Context;

import com.tokopedia.loginregister.login.view.listener.LoginContract;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.view.LoginCommonSubscriber;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 10/16/18.
 */
public class LoginSubscriber extends LoginCommonSubscriber<LoginEmailDomain> {

    private final LoginContract.View view;

    public LoginSubscriber(Context context, LoginSuccessRouter router,
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
        view.enableArrow();
        super.onError(e);
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        super.onNext(loginEmailDomain);
    }
}
