package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.OutletModule;
import com.tokopedia.posapp.di.module.ShopModule;
import com.tokopedia.posapp.di.scope.OutletScope;
import com.tokopedia.posapp.di.scope.ShopScope;
import com.tokopedia.posapp.outlet.view.fragment.OutletFragment;

import dagger.Component;

/**
 * Created by okasurya on 7/31/17.
 */

@OutletScope
@ShopScope
@Component(modules = {ShopModule.class, OutletModule.class}, dependencies = AppComponent.class)
public interface OutletComponent {
    void inject(OutletFragment outletFragment);
}
