package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.ReactCacheModule;
import com.tokopedia.posapp.di.scope.ReactCacheScope;
import com.tokopedia.posapp.react.reactmodule.PosCacheRNModule;
import com.tokopedia.posapp.react.reactmodule.ProductDiscoveryRNModule;

import dagger.Component;

/**
 * Created by okasurya on 9/15/17.
 */

@ReactCacheScope
@Component(
        modules = ReactCacheModule.class,
        dependencies = AppComponent.class
)
public interface ReactCacheComponent {
    void inject(PosCacheRNModule posCacheRNModule);

    void inject(ProductDiscoveryRNModule productDiscoveryRNModule);
}
