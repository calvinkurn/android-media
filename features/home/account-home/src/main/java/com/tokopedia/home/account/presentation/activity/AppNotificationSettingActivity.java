package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.home.account.presentation.fragment.setting.AppNotificationSettingFragment;

public class AppNotificationSettingActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, AppNotificationSettingActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return AppNotificationSettingFragment.createInstance();
    }
}
