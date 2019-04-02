package com.tokopedia.loginphone.checkloginphone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.checkloginphone.view.fragment.NotConnectedTokocashFragment;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;


/**
 * @author by nisie on 12/4/17.
 */

public class NotConnectedTokocashActivity extends BaseSimpleActivity {

    public static final String PARAM_PHONE_NUMBER = "phone";
    public static final String PARAM_TYPE = "type";
    public static final int TYPE_PHONE_NOT_CONNECTED = 1;
    public static final int TYPE_NO_TOKOCASH_ACCOUNT = 2;

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        return NotConnectedTokocashFragment.createInstance(bundle);
    }


    @Override
    public String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_NOT_CONNECTED_TO_TOKOCASH;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt(PARAM_TYPE) ==
                TYPE_PHONE_NOT_CONNECTED) {
            toolbar.setTitle(R.string.phone_number_not_connected);
        } else if (getIntent().getExtras() != null && getIntent().getExtras().getInt(PARAM_TYPE) ==
                TYPE_NO_TOKOCASH_ACCOUNT) {
            toolbar.setTitle(R.string.no_account_connected);
        }
    }

    public static Intent getPhoneNumberNotConnectedIntent(Context context, String phoneNumber) {
        Intent intent = new Intent(context, NotConnectedTokocashActivity.class);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtra(PARAM_TYPE, TYPE_PHONE_NOT_CONNECTED);
        return intent;
    }

    public static Intent getNoTokocashAccountIntent(Context context, String phoneNumber) {
        Intent intent = new Intent(context, NotConnectedTokocashActivity.class);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtra(PARAM_TYPE, TYPE_NO_TOKOCASH_ACCOUNT);
        return intent;
    }
}
