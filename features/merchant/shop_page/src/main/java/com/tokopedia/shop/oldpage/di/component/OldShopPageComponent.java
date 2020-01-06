package com.tokopedia.shop.oldpage.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.oldpage.di.module.OldShopPageModule;
import com.tokopedia.shop.oldpage.di.scope.OldShopPageScope;
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@OldShopPageScope
@Component(modules = OldShopPageModule.class, dependencies = ShopComponent.class)
public interface OldShopPageComponent {

    void inject(ShopPageActivity shopInfoActivity);

}
