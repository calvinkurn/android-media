package com.tokopedia.loginphone.chooseaccount.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.chooseaccount.view.fragment.ChooseAccountFragment;

/**
 * @author by nisie on 12/4/17.
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal#CHOOSE_ACCOUNT
 */

public class ChooseAccountActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        return ChooseAccountFragment.Companion.createInstance(bundle);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        toolbar.setPadding(0, 0, 30, 0);
    }

    @Override
    public String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT;
    }

    public static Intent getCallingIntent(Context context, ChooseAccountViewModel model) {
        Intent intent = new Intent(context, ChooseAccountActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

}
