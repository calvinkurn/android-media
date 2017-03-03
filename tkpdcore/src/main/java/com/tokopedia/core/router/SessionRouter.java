package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by stevenfredian on 11/23/16.
 */

public class SessionRouter {

    private static final String LOGIN_ACTIVITY = "com.tokopedia.session.session.activity.Login";

    public static final String IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT = "RegisterNewNextFragment";
    public static final String IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT = "RegisterPassPhoneFragment";
    private static final String CHANGE_PHONE_NUMBER_REQUEST_ACTIVITY = "com.tokopedia.session.changephonenumber.activity.ChangePhoneNumberRequestActivity";

    /////////// INTENT
    public static Intent getLoginActivityIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, LOGIN_ACTIVITY);
        return intent;
    }

    public static Intent getChangePhoneNumberRequestActivity(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, CHANGE_PHONE_NUMBER_REQUEST_ACTIVITY);
        return intent;
    }
}
