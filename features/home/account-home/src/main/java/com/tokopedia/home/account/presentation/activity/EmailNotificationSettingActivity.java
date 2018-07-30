package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.home.account.di.component.EmailNotificationSettingComponent;
import com.tokopedia.home.account.di.component.DaggerEmailNotificationSettingComponent;
import com.tokopedia.home.account.presentation.fragment.setting.EmailNotificationSettingFragment;

public class EmailNotificationSettingActivity extends BaseSimpleActivity
        implements HasComponent<EmailNotificationSettingComponent>{

    public static Intent createIntent(Context context) {
        return new Intent(context, EmailNotificationSettingActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return EmailNotificationSettingFragment.createInstance();
    }

    @Override
    public EmailNotificationSettingComponent getComponent() {
        return DaggerEmailNotificationSettingComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();
    }
}
