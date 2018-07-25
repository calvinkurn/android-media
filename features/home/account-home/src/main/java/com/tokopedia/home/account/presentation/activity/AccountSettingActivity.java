package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.home.account.presentation.fragment.setting.AccountSettingFragment;

public class AccountSettingActivity extends BaseSimpleActivity {
    public static Intent createIntent(Context context) {
        return new Intent(context, AccountSettingActivity.class);
    }
    @Override
    protected Fragment getNewFragment() {
        return AccountSettingFragment.createInstance();
    }
}
