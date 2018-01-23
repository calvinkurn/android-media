package com.tokopedia.core.referral;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.referral.di.ReferralComponent;
import com.tokopedia.core.referral.fragment.FragmentReferral;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.referral.di.DaggerReferralComponent;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralActivity extends BasePresenterActivity implements HasComponent<ReferralComponent> {

    public static final String screenName = "Referral Home";
    ReferralComponent referralComponent = null;

    @DeepLink(Constants.Applinks.REFERRAL)
    public static Intent getCallingReferral(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ReferralActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackingUtils.sendMoEngageReferralScreenOpen(screenName);

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
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof FragmentReferral))
            getFragmentManager().beginTransaction().replace(R.id.container,
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
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFERRAL_TITLE, getString(R.string.drawer_title_appshare));
    }
}