package com.tokopedia.shop.pageheader.di.component;

import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.pageheader.di.module.ShopPageModule;
import com.tokopedia.shop.pageheader.di.scope.ShopPageScope;
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageScope
@Component(modules = ShopPageModule.class, dependencies = ShopComponent.class)
public interface ShopPageComponent {

    void inject(ShopPageFragment shopPageFragment);

    @Named(GQLQueryNamedConstant.GET_IS_OFFICIAL)
    String getGqlIsShopOsQuery();

    @Named(GQLQueryNamedConstant.GET_IS_POWER_MERCHANT)
    String getGqlIsShopPowerMerchantQuery();

}
