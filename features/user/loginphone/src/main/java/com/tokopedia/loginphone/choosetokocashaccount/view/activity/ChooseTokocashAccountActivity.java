package com.tokopedia.loginphone.choosetokocashaccount.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loginphone.choosetokocashaccount.data.ChooseTokoCashAccountViewModel;
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.loginphone.choosetokocashaccount.view.fragment.ChooseTokocashAccountFragment;

/**
 * @author by nisie on 12/4/17.
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal#CHOOSE_ACCOUNT
 */

public class ChooseTokocashAccountActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        return ChooseTokocashAccountFragment.createInstance(bundle);
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

    public static Intent getCallingIntent(Context context, ChooseTokoCashAccountViewModel model) {
        Intent intent = new Intent(context, ChooseTokocashAccountActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

}
