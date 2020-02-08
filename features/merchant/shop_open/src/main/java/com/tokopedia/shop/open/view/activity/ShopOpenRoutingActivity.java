package com.tokopedia.shop.open.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.shop.open.shop_open_revamp.presentation.view.activity.ShopOpenRevampActivity;
import com.tokopedia.shop.open.view.fragment.ShopOpenRoutingFragment;

/**
 * Created by nathan on 12/19/17.
 * For navigating: use ApplinkConstInternalMarketplace.OPEN_SHOP
 */
public class ShopOpenRoutingActivity extends BaseSimpleActivity implements HasComponent<ShopOpenDomainComponent> {

    private RemoteConfig remoteConfig;
    private boolean enableOPenShopRevamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remoteConfig = new FirebaseRemoteConfigImpl(this);
        enableOPenShopRevamp = remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_OPEN_SHOP_REVAMP, true);

        boolean isNeedLocation = false;
        if (getIntent().getExtras() != null) {
            isNeedLocation = getIntent().getBooleanExtra(ApplinkConstInternalMarketplace.PARAM_IS_NEED_LOC, false);
        }

        if (enableOPenShopRevamp) {
            Intent intent = new Intent(this, ShopOpenRevampActivity.class);
            intent.putExtra(ApplinkConstInternalMarketplace.PARAM_IS_NEED_LOC, isNeedLocation);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopOpenRoutingFragment.newInstance();
    }

    @Override
    public ShopOpenDomainComponent getComponent() {
        return DaggerShopOpenDomainComponent
                .builder()
                .shopOpenDomainModule(new ShopOpenDomainModule())
                .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                .build();
    }
}