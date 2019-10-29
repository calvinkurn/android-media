package com.tokopedia.referral.view.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.referral.R;
import com.tokopedia.referral.di.DaggerReferralComponent;
import com.tokopedia.referral.di.ReferralComponent;
import com.tokopedia.referral.view.fragment.FragmentReferralFriendsWelcome;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.List;

public class FriendsWelcomeActivity extends BaseSimpleActivity implements HasComponent<ReferralComponent> {


    private static final String WELCOME_SCREEN = "/referral/friends";
    private ReferralComponent referralComponent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String toolbarTitle = getString(R.string.referral);
        if (!TextUtils.isEmpty(toolbarTitle)) updateTitle(toolbarTitle);
        if (!isappShowReferralButtonActivated(this)) {
            finish();
        }
    }

    @Override
    public String getScreenName() {
        return WELCOME_SCREEN;
    }

    private Boolean isappShowReferralButtonActivated(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        return remoteConfig.getBoolean(RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);
    }

    @Override
    public ReferralComponent getComponent() {
        if (referralComponent == null) {
            initInjector();
        }
        return referralComponent;
    }

    private void initInjector() {
        referralComponent = DaggerReferralComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplicationContext()).getBaseAppComponent()).build();
    }

    @Override
    protected androidx.fragment.app.Fragment getNewFragment() {
        Uri uri = getIntent().getData();
        List<String> linkPathSegments = UriUtil.destructureUri(
                ApplinkConstInternalGlobal.REFERRAL_WELCOME_FRIENDS, uri, true);
        String code = linkPathSegments.get(0).isEmpty() ? "" : linkPathSegments.get(0);
        String owner = linkPathSegments.get(1).isEmpty() ? "" : linkPathSegments.get(1);
        return FragmentReferralFriendsWelcome.newInstance(code, owner);
    }
}