package com.tokopedia.posapp.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.view.fragment.OutletFragment;

import dagger.Component;

/**
 * Created by okasurya on 7/31/17.
 */

@OutletScope
@Component(modules = OutletModule.class, dependencies = AppComponent.class)
public interface OutletComponent {
    void inject(OutletFragment outletFragment);
}
