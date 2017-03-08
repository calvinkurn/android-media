package com.tokopedia.core.router;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by stevenfredian on 11/23/16.
 */

public class SessionRouter {

    private static final String LOGIN_ACTIVITY = "com.tokopedia.session.session.activity.Login";

    private static final String PHONE_VERIFICATION_ACTIVATION_ACTIVITY = "com.tokopedia.otp.phoneverification.activity.PhoneVerificationActivationActivity";
    private static final String PHONE_VERIFICATION_PROFILE_ACTIVITY = "com.tokopedia.otp.phoneverification.activity.PhoneVerificationProfileActivity";



    public static final String IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT = "RegisterNewNextFragment";
    public static final String IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT = "RegisterPassPhoneFragment";
    public static final int REQUEST_VERIFY_PHONE = 123;

    /////////// INTENT
    public static Intent getLoginActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, LOGIN_ACTIVITY);
        return intent;
    }

    public static Intent getPhoneVerificationActivationActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, PHONE_VERIFICATION_ACTIVATION_ACTIVITY);
        return intent;
    }

    public static Intent getPhoneVerificationProfileActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, PHONE_VERIFICATION_PROFILE_ACTIVITY);
        return intent;
    }
}
