package com.tokopedia.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;

/**
 * For navigate: use {@link ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER}
 * Please give param ApplinkConstInternalGlobal.PARAM_EMAIL
 * and ApplinkConstInternalGlobal.PARAM_MSISDN
 */
public class ChangePhoneNumberWarningActivity extends BaseSimpleActivity {

    private String email;
    private String phoneNumber;

    public static Intent newInstance(Context context, String email, String phoneNumber) {
        Intent intent = new Intent(context, ChangePhoneNumberWarningActivity.class);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        } else {
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    private void setupBundlePass(Bundle extras) {
        email = extras.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "");
        phoneNumber = extras.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "");
        if (email.isEmpty() || phoneNumber.isEmpty()) {
            finish();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return ChangePhoneNumberWarningFragment.newInstance(email, phoneNumber);
    }


}
