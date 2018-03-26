package com.tokopedia.posapp.outlet.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.shop.di.ShopModule;
import com.tokopedia.posapp.shop.di.ShopScope;
import com.tokopedia.posapp.outlet.view.fragment.OutletFragment;

import dagger.Component;

/**
 * Created by okasurya on 7/31/17.
 */

@OutletScope
@ShopScope
@Component(modules = {ShopModule.class, OutletModule.class}, dependencies = BaseAppComponent.class)
public interface OutletComponent {
    void inject(OutletFragment outletFragment);
}
