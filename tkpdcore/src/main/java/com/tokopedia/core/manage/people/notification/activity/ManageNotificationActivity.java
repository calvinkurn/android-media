package com.tokopedia.core.manage.people.notification.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.notification.fragment.ManageNotificationFragment;

/**
 * Created by Nisie on 6/22/16.
 */
public class ManageNotificationActivity extends BasePresenterActivity {

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_P_NOTIF;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(
                ManageNotificationFragment.class.getSimpleName());

        if (fragment == null) {
            fragment = ManageNotificationFragment.createInstance(bundle);
        }

        fragmentTransaction.replace(R.id.container, fragment, ManageNotificationFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
