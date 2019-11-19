package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.home.account.presentation.fragment.setting.StoreSettingFragment;

public class StoreSettingActivity extends BaseSimpleActivity {
    public static Intent createIntent(Context context) {
        return new Intent(context, StoreSettingActivity.class);
    }
    @Override
    protected Fragment getNewFragment() {
        return StoreSettingFragment.createInstance();
    }
}
