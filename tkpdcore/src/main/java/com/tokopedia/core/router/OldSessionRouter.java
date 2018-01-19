package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by stevenfredian on 11/23/16.
 */

@Deprecated
public class OldSessionRouter {

    private static final String LOGIN_ACTIVITY = "com.tokopedia.session.session.activity.Login";

    private static final String PHONE_VERIFICATION_PROFILE_ACTIVITY = "com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationProfileActivity";
    private static final String RELOGIN_ACTIVITY = "com.tokopedia.session.login.view.ReloginActivity";


    public static final String IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT = "RegisterNewNextFragment";
    public static final String IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT = "RegisterPassPhoneFragment";
    private static final String CHANGE_PHONE_NUMBER_REQUEST_ACTIVITY = "com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberRequestActivity";
    public static final String PARAM_FORCE_LOGOUT_MESSAGE = "PARAM_FORCE_LOGOUT_MESSAGE";

    /////////// INTENT
    public static Intent getLoginActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, LOGIN_ACTIVITY);
        return intent;
    }

    public static Intent getPhoneVerificationActivationActivityIntent(Context context) {
        return RouterUtils.getRouterFromContext(context).getPhoneVerificationActivityIntent(context);
    }

    public static Intent getPhoneVerificationProfileActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, PHONE_VERIFICATION_PROFILE_ACTIVITY);
        return intent;
    }

    public static Intent getReloginActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, RELOGIN_ACTIVITY);
        return intent;
    }

    public static Class<?> getLoginActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(LOGIN_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    public static Intent getChangePhoneNumberRequestActivity(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, CHANGE_PHONE_NUMBER_REQUEST_ACTIVITY);
        return intent;
    }
}
