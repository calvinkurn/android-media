package com.tokopedia.loginphone.checkloginphone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginphone.checkloginphone.view.fragment.CheckLoginPhoneNumberFragment;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;

/**
 * @author by nisie on 11/23/17.
 */

public class CheckLoginPhoneNumberActivity extends BaseSimpleActivity {

    public final static String PARAM_PHONE_NUMBER = "phone_number";

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        return CheckLoginPhoneNumberFragment.createInstance(bundle);
    }

    @Override
    public String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_LOGIN_PHONE_NUMBER;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CheckLoginPhoneNumberActivity.class);
    }

    public static Intent getCallingIntentFromRegister(Context context, String phoneNumber) {
        Intent intent = new Intent(context, CheckLoginPhoneNumberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtras(bundle);
        return intent;
    }

}
