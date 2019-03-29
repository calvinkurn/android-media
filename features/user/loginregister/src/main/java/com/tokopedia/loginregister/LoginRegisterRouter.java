package com.tokopedia.loginregister;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * @author by nisie on 10/2/18.
 */
public interface LoginRegisterRouter {
    Intent getForgotPasswordIntent(Context context, String email);

    void setTrackingUserId(String userId, Context applicationContext);

    void setMoEUserAttributesLogin(String userId, String name, String email,
                                   String phoneNumber, boolean isGoldMerchant,
                                   String shopName, String shopId,
                                   boolean hasShop, String loginMethod);

    void eventMoRegistrationStart(String labelEmail);

    void sendAFCompleteRegistrationEvent(int userId, String methodName);

    void eventMoRegister(String name, String phone);

    void sendBranchRegisterEvent(String email, String phone);

    void onLoginSuccess();
}
