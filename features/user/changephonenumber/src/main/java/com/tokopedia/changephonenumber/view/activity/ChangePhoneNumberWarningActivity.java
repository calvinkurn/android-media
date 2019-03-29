package com.tokopedia.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;


public class ChangePhoneNumberWarningActivity extends BaseSimpleActivity {
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PHONE_NUMBER = "phone_number";

    private String email;
    private String phoneNumber;

    public static Intent newInstance(Context context, String email, String phoneNumber) {
        Intent intent = new Intent(context, ChangePhoneNumberWarningActivity.class);
        intent.putExtra(PARAM_EMAIL, email);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupBundlePass(getIntent().getExtras());
        super.onCreate(savedInstanceState);
    }

    private void setupBundlePass(Bundle extras) {
        email = extras.getString(PARAM_EMAIL);
        phoneNumber = extras.getString(PARAM_PHONE_NUMBER);
    }

    @Override
    protected Fragment getNewFragment() {
        return ChangePhoneNumberWarningFragment.newInstance(email, phoneNumber);
    }


}
