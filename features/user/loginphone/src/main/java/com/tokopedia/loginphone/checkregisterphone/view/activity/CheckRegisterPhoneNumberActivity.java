package com.tokopedia.loginphone.checkregisterphone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginphone.checkregisterphone.view.fragment.CheckRegisterPhoneNumberFragment;

/**
 * @author by yfsx on 26/2/18.
 */

public class CheckRegisterPhoneNumberActivity extends BaseSimpleActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CheckRegisterPhoneNumberActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        return CheckRegisterPhoneNumberFragment.createInstance(bundle);
    }
}
