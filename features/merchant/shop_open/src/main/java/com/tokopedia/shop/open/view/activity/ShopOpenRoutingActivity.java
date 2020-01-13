package com.tokopedia.shop.open.view.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.shop.open.view.fragment.ShopOpenRoutingFragment;

/**
 * Created by nathan on 12/19/17.
 * For navigating: use ApplinkConstInternalMarketplace.OPEN_SHOP
 */
public class ShopOpenRoutingActivity extends BaseSimpleActivity implements HasComponent<ShopOpenDomainComponent> {

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