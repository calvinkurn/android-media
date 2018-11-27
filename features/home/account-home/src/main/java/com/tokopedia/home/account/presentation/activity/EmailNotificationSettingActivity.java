package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.home.account.di.component.EmailNotificationSettingComponent;
import com.tokopedia.home.account.di.component.DaggerEmailNotificationSettingComponent;
import com.tokopedia.home.account.presentation.fragment.setting.EmailNotificationSettingFragment;

public class EmailNotificationSettingActivity extends BaseSimpleActivity
        implements HasComponent<EmailNotificationSettingComponent> {

    public static Intent createIntent(Context context) {
        return new Intent(context, EmailNotificationSettingActivity.class);
    }

    private Fragment fragment;

    @Override
    protected Fragment getNewFragment() {
        fragment = EmailNotificationSettingFragment.createInstance();
        return fragment;
    }

    @Override
    public EmailNotificationSettingComponent getComponent() {
        return DaggerEmailNotificationSettingComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fragment != null)
            ((OnBackPressedFragmentListener) fragment).onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public interface OnBackPressedFragmentListener {
        void onBackPressed();
    }
}
