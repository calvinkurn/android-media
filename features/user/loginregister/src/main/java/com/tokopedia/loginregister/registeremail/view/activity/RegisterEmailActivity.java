package com.tokopedia.loginregister.registeremail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;

import javax.inject.Inject;

/**
 * @author by nisie on 10/25/18.
 */
public class RegisterEmailActivity extends BaseSimpleActivity {

    public static final String EXTRA_PARAM_EMAIL = "email";

    @Inject
    LoginRegisterAnalytics analytics;

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if(getIntent().getExtras()!= null){
            bundle.putAll(getIntent().getExtras());
        }
        return null;
    }


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterEmailActivity.class);
    }

    public static Intent getCallingIntentWithEmail(@NonNull Context context, @NonNull String email) {
        Intent intent = new Intent(context, RegisterEmailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_EMAIL, email);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onBackPressed() {
        analytics.eventClickBackRegisterWithEmail();
        super.onBackPressed();
    }
}
