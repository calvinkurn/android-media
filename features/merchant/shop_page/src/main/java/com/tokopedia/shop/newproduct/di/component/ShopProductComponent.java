package com.tokopedia.shop.newproduct.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.newproduct.view.fragment.ShopPageProductListFragment;
import com.tokopedia.shop.newproduct.view.fragment.ShopPageProductListResultFragment;
import com.tokopedia.shop.newproduct.di.module.ShopProductModule;
import com.tokopedia.shop.newproduct.di.scope.ShopProductScope;
import com.tokopedia.shop.newproduct.view.fragment.HomeProductFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopProductScope
@Component(modules = ShopProductModule.class, dependencies = ShopComponent.class)
public interface ShopProductComponent {
    void inject(ShopPageProductListFragment shopPageProductFragment);
    void inject(HomeProductFragment homeProductFragment);
    void inject(ShopPageProductListResultFragment  shopPageProductListResultFragment);

}
