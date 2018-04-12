package com.tokopedia.shop.etalase.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.etalase.di.module.ShopEtalaseModule;
import com.tokopedia.shop.etalase.di.scope.ShopEtalaseScope;
import com.tokopedia.shop.etalase.view.fragment.ShopEtalaseFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopEtalaseScope
@Component(modules = ShopEtalaseModule.class, dependencies = ShopComponent.class)
public interface ShopEtalaseComponent {

    void inject(ShopEtalaseFragment shopEtalaseFragment);

}
