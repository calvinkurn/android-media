package com.tokopedia.forgotpassword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.forgotpassword.analytics.ForgotPasswordAnalytics;
import com.tokopedia.forgotpassword.view.fragment.ForgotPasswordFragment;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordActivity extends BaseSimpleActivity {

    private static final String TAG = "FORGOT_PASSWORD_FRAGMENT";
    private static final String INTENT_EXTRA_PARAM_EMAIL = "INTENT_EXTRA_PARAM_EMAIL";
    private static final String INTENT_EXTRA_AUTO_RESET = "INTENT_EXTRA_AUTO_RESET";
    private static final String INTENT_EXTRA_REMOVE_FOOTER = "INTENT_EXTRA_REMOVE_FOOTER";

    /**
     * @param context activity context
     * @return Intent
     * default intent
     */
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    /**
     * @param context activity context
     * @param email user email
     * @return Intent
     * use this for auto filled email
     */
    public static Intent getCallingIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        return intent;
    }

    /**
     * @param context activity context
     * @param email user email
     * @return Intent
     * use this for auto reset password
     */
    public static Intent getAutomaticResetPasswordIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        intent.putExtra(INTENT_EXTRA_AUTO_RESET, true);
        intent.putExtra(INTENT_EXTRA_REMOVE_FOOTER, true);
        return intent;
    }

    @Override
    public String getScreenName() {
        return ForgotPasswordAnalytics.Screen.FORGOT_PASSWORD;
    }

    @Override
    protected Fragment getNewFragment() {

        if (getIntent() != null && getIntent().getExtras() != null) {
            return ForgotPasswordFragment.createInstance(
                    getIntent().getExtras().getString(INTENT_EXTRA_PARAM_EMAIL, ""),
                    getIntent().getExtras().getBoolean(INTENT_EXTRA_AUTO_RESET, false),
                    getIntent().getExtras().getBoolean(INTENT_EXTRA_REMOVE_FOOTER, false)
            );
        } else {
            return null;
        }
    }
}
