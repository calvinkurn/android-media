package com.tokopedia.shop.page.di.component;

import com.tokopedia.shop.address.view.fragment.ShopAddressListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;
import com.tokopedia.shop.page.di.module.ShopPageModule;
import com.tokopedia.shop.page.di.scope.ShopPageScope;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageScope
@Component(modules = ShopPageModule.class, dependencies = ShopComponent.class)
public interface ShopPageComponent {

    void inject(ShopPageActivity shopInfoActivity);

}
