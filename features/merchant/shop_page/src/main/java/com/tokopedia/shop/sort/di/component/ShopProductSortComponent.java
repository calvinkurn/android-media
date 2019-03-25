package com.tokopedia.shop.sort.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.sort.di.module.ShopProductSortModule;
import com.tokopedia.shop.sort.di.scope.ShopProductSortScope;
import com.tokopedia.shop.sort.view.fragment.ShopProductSortFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopProductSortScope
@Component(modules = ShopProductSortModule.class, dependencies = ShopComponent.class)
public interface ShopProductSortComponent {

    void inject(ShopProductSortFragment shopProductFilterFragment);

}
