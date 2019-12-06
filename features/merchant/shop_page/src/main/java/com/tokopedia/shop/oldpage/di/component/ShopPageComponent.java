package com.tokopedia.shop.oldpage.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.oldpage.di.module.ShopPageModule;
import com.tokopedia.shop.oldpage.di.scope.ShopPageScope;
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageScope
@Component(modules = ShopPageModule.class, dependencies = ShopComponent.class)
public interface ShopPageComponent {

    void inject(ShopPageActivity shopInfoActivity);

}
