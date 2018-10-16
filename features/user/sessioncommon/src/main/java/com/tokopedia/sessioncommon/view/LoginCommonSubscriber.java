package com.tokopedia.sessioncommon.view;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.sessioncommon.ErrorHandlerSession;
import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.LoginEmailDomain;
import com.tokopedia.sessioncommon.data.model.MakeLoginPojo;

import rx.Subscriber;

/**
 * @author by nisie on 12/19/17.
 */

public class LoginCommonSubscriber<T> extends Subscriber<LoginEmailDomain> {
    private static final String NOT_ACTIVATED = "belum diaktivasi";
    protected final LoginSuccessRouter router;
    protected final String email;
    private final Context context;

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
        if (e.getLocalizedMessage() != null
                && e.getLocalizedMessage().toLowerCase().contains(NOT_ACTIVATED)
                && !TextUtils.isEmpty(email)) {
            router.onGoToActivationPage(email);
        } else {
            ErrorHandlerSession.getErrorMessage(new ErrorHandlerSession.ErrorForbiddenListener() {
                @Override
                public void onForbidden() {
                    router.onForbidden();
                }

                @Override
                public void onError(String errorMessage) {
                    router.onErrorLogin(errorMessage);
                }
            }, e, context);
        }
    }

    @Override
    public void onNext(LoginEmailDomain loginEmailDomain) {
        if (!loginEmailDomain.getInfo().isCreatedPassword()
                && GlobalConfig.isSellerApp()) {
            router.onGoToCreatePasswordPage(loginEmailDomain.getInfo());
        } else if (loginEmailDomain.getLoginResult() != null
                && !goToSecurityQuestion(loginEmailDomain.getLoginResult())
                && (!router.isFromRegister() || GlobalConfig.isSellerApp())) {
            router.setSmartLock();
            router.onSuccessLoginEmail();
        } else if (loginEmailDomain.getLoginResult() != null
                && !goToSecurityQuestion(loginEmailDomain.getLoginResult())
                && !isMsisdnVerified(loginEmailDomain.getInfo())
                && router.isFromRegister()) {
            router.setSmartLock();
            router.onGoToPhoneVerification();
        } else if (goToSecurityQuestion(loginEmailDomain.getLoginResult())) {
            router.setSmartLock();
            router.onGoToSecurityQuestion(
                    loginEmailDomain.getLoginResult().getSecurityPojo(),
                    loginEmailDomain.getLoginResult().getFullName(),
                    loginEmailDomain.getInfo().getEmail(),
                    loginEmailDomain.getInfo().getPhone());
        } else {
            router.onErrorLogin(ErrorHandlerSession.getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode
                    .UNSUPPORTED_FLOW, context));
        }
    }

    private boolean isMsisdnVerified(GetUserInfoData info) {
        return info.isPhoneVerified();
    }

    private boolean goToSecurityQuestion(MakeLoginPojo makeLoginPojo) {
        return makeLoginPojo.getSecurityPojo().getAllowLogin() == 0
                && !makeLoginPojo.getIsLogin().equals("1");
    }
}
