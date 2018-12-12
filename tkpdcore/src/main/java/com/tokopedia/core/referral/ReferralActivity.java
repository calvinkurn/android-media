package com.tokopedia.core.referral;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.referral.di.DaggerReferralComponent;
import com.tokopedia.core.referral.di.ReferralComponent;
import com.tokopedia.core.referral.fragment.FragmentReferral;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralActivity extends BasePresenterActivity implements HasComponent<ReferralComponent> {

    private static final String REFERRAL_SCREEN = "/referral";
    ReferralComponent referralComponent = null;

    @DeepLink(Constants.Applinks.REFERRAL)
    public static Intent getCallingReferral(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ReferralActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent getCallingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ReferralActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

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
        TrackingUtils.sendMoEngageReferralScreenOpen(this, getString(R.string.referral_screen_name));
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
        return R.layout.activity_referral;
    }

    @Override
    protected void initView() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof FragmentReferral))
            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                    FragmentReferral.newInstance()).commit();
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
    public ReferralComponent getComponent() {
        if (referralComponent == null) {
            initInjector();
        }
        return referralComponent;
    }


    private void initInjector() {
        referralComponent = DaggerReferralComponent.builder()
                .appComponent(getApplicationComponent())
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        invalidateTitleToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateTitleToolBar();
    }

    private void invalidateTitleToolBar() {
        String titleToolbar = getToolbarTitle();
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private String getToolbarTitle() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(ReferralActivity.this);
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON)) {
            return remoteConfig.getString(RemoteConfigKey.APP_REFERRAL_TITLE, getString(R.string.drawer_title_referral_appshare));
        } else {
            return getString(R.string.drawer_title_appshare);
        }
    }
}