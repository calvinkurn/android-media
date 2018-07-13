package com.tokopedia.posapp.outlet.di;

import com.tokopedia.posapp.di.component.PosAppComponent;
import com.tokopedia.posapp.outlet.view.fragment.OutletFragment;
import com.tokopedia.posapp.shop.di.ShopModule;
import com.tokopedia.posapp.shop.di.ShopScope;

import dagger.Component;

/**
 * Created by okasurya on 7/31/17.
 */

@OutletScope
@ShopScope
@Component(modules = {ShopModule.class, OutletModule.class}, dependencies = PosAppComponent.class)
public interface OutletComponent {
    void inject(OutletFragment outletFragment);
}
