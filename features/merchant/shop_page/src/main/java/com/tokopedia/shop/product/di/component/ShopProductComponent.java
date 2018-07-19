package com.tokopedia.shop.product.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.di.scope.ShopProductScope;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragmentOld;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedNewFragment;
import com.tokopedia.shop.product.view.fragment.ShopProductListLimitedFragmentOld;
import com.tokopedia.shop.product.view.fragment.ShopProductListNewFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopProductScope
@Component(modules = ShopProductModule.class, dependencies = ShopComponent.class)
public interface ShopProductComponent {

    void inject(ShopProductListLimitedFragmentOld shopProductListLimitedFragmentOld);

    void inject(ShopProductListFragmentOld shopProductListFragment);
    void inject(ShopProductListLimitedNewFragment shopProductListLimitedNewFragment);
    void inject(ShopProductListNewFragment shopProductListNewFragment);

}
