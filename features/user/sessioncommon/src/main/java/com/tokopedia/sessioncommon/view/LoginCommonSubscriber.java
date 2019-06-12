package com.tokopedia.sessioncommon.view;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;
import com.tokopedia.sessioncommon.network.TokenErrorException;
import com.tokopedia.sessioncommon.R;

import rx.Subscriber;

/**
 * @author by nisie on 12/19/17.
 */

public abstract class LoginCommonSubscriber<T> extends Subscriber<T> {
    private static final String NOT_ACTIVATED = "belum diaktivasi";
    protected final LoginSuccessRouter router;
    protected final String email;
    protected final Context context;

    public LoginCommonSubscriber(Context context,
                                 LoginSuccessRouter router,
                                 String email) {
        this.context = context;
        this.router = router;
        this.email = email;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (isEmailNotActive(e, email)) {
            router.onGoToActivationPage(email);
        } else if (e instanceof TokenErrorException
                && !((TokenErrorException) e).getErrorDescription().isEmpty()) {
            router.onErrorLogin(((TokenErrorException) e).getErrorDescription());
        } else {
            ErrorHandlerSession.getErrorMessage(new ErrorHandlerSession.ErrorForbiddenListener() {
                @Override
                public void onForbidden() {
                    router.onForbidden();
                }

                @Override
                public void onError(String errorMessage) {
                    onErrorLogin(errorMessage);

                    if (!TextUtils.isEmpty(e.getMessage())
                            && errorMessage.contains(context.getString(R.string
                            .default_request_error_unknown))) {
                        router.logUnknownError(e);
                    }
                }
            }, e, context);
        }
    }

    protected boolean isEmailNotActive(Throwable e, String email) {
        return e instanceof TokenErrorException
                && !((TokenErrorException) e).getErrorDescription().isEmpty()
                && ((TokenErrorException) e).getErrorDescription()
                .toLowerCase().contains(NOT_ACTIVATED)
                && !TextUtils.isEmpty(email);
    }

    protected void onErrorLogin(String errorMessage) {
        router.onErrorLogin(errorMessage);
    }

    @Override
    public void onNext(T result) {
        if (canRouteLogin(result)) {
            routeLogin((LoginEmailDomain) result);
        }
    }

    protected boolean canRouteLogin(T result) {
        return result != null;
    }

    protected void routeLogin(LoginEmailDomain loginEmailDomain) {
        if (isGoToSecurityQuestion(loginEmailDomain.getLoginResult())) {
            goToSecurityQuestion(loginEmailDomain);
        } else if (isGoToCreatePassword(loginEmailDomain)) {
            goToCreatePassword(loginEmailDomain);
        } else if (isGoToPhoneVerification(loginEmailDomain)) {
            goToPhoneVerification();
        }
    }

    protected boolean isGoToCreatePassword(LoginEmailDomain loginEmailDomain) {
        return !loginEmailDomain.getInfo().isCreatedPassword()
                && GlobalConfig.isSellerApp();
    }


    protected boolean isGoToPhoneVerification(LoginEmailDomain loginEmailDomain) {
        return loginEmailDomain.getLoginResult() != null
                && !isMsisdnVerified(loginEmailDomain.getInfo());
    }

    protected boolean isGoToSecurityQuestion(MakeLoginPojo makeLoginPojo) {
        return makeLoginPojo.getSecurityPojo().getAllowLogin() == 0
                && !makeLoginPojo.getIsLogin().equals("true");
    }


    protected void goToSecurityQuestion(LoginEmailDomain loginEmailDomain) {
        router.onGoToSecurityQuestion(
                loginEmailDomain.getInfo().getEmail(),
                loginEmailDomain.getInfo().getPhone());
    }

    protected void goToPhoneVerification() {
        router.onGoToPhoneVerification();
    }


    protected void goToCreatePassword(LoginEmailDomain loginEmailDomain) {
        router.onGoToCreatePasswordPage(loginEmailDomain.getInfo());
    }

    protected boolean isMsisdnVerified(GetUserInfoData info) {
        return info.isPhoneVerified();
    }
}

