package com.tokopedia.sessioncommon.view;

import com.tokopedia.sessioncommon.data.model.GetUserInfoData;
import com.tokopedia.sessioncommon.data.model.SecurityPojo; /**
 * @author by nisie on 10/16/18.
 */
public interface LoginSuccessRouter {
    void onGoToActivationPage(String email);

    void onForbidden();

    void onErrorLogin(String errorMessage);

    void onGoToCreatePasswordPage(GetUserInfoData info);

    void onGoToPhoneVerification();

    void onGoToSecurityQuestion(SecurityPojo securityPojo, String fullName, String email, String phone);

    void logUnknownError(Throwable message);
}
