package com.tokopedia.shop.home.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope;
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeNplCampaignTncBottomSheet;
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment;
import com.tokopedia.shop.home.di.module.ShopPageHomeModule;
import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopPageHomeScope
@Component(modules = ShopPageHomeModule.class, dependencies = ShopComponent.class)
public interface ShopPageHomeComponent {
    void inject(ShopPageHomeFragment fragment);

    void inject(ShopHomeNplCampaignTncBottomSheet bottomSheet);

}
