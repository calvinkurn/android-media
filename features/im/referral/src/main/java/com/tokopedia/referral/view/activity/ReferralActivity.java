package com.tokopedia.referral.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.referral.R;
import com.tokopedia.referral.analytics.ReferralAnalytics;
import com.tokopedia.referral.di.DaggerReferralComponent;
import com.tokopedia.referral.di.ReferralComponent;
import com.tokopedia.referral.view.fragment.FragmentReferral;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralActivity extends BaseSimpleActivity {

    private static final String REFERRAL_SCREEN = "/referral";

    @Inject
    ReferralAnalytics referralAnalytics;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, ReferralActivity.class);
        return intent;
    }

    @Override
    public String getScreenName() {
        return REFERRAL_SCREEN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String toolbarTitle = getToolbarTitle();
        if (!TextUtils.isEmpty(toolbarTitle)) updateTitle(toolbarTitle);

        initInjector();

        referralAnalytics.sendMoEngageReferralScreenOpen(getString(R.string.referral_screen_name));
    }

    private void initInjector() {
        ReferralComponent referralComponent = DaggerReferralComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplicationContext()).getBaseAppComponent()).build();
        referralComponent.inject(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String getToolbarTitle() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(ReferralActivity.this);
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON)) {
            return remoteConfig.getString(RemoteConfigKey.APP_REFERRAL_TITLE, getString(R.string.drawer_title_referral_appshare));
        } else {
            return getString(R.string.drawer_title_appshare);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return FragmentReferral.newInstance();
    }
}