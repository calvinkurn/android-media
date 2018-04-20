package com.tokopedia.posapp.react.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.react.di.module.ReactDataModule;
import com.tokopedia.posapp.react.di.scope.ReactDataScope;
import com.tokopedia.posapp.react.reactmodule.DataReactModule;
import com.tokopedia.posapp.react.reactmodule.ProductDiscoveryReactModule;

import dagger.Component;

/**
 * Created by okasurya on 9/15/17.
 */

@ReactDataScope
@Component(
        modules = ReactDataModule.class,
        dependencies = BaseAppComponent.class
)
public interface ReactDataComponent {
    void inject(DataReactModule dataReactModule);

    void inject(ProductDiscoveryReactModule productDiscoveryRNModule);
}
