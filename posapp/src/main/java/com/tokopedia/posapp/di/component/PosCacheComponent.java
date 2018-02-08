package com.tokopedia.posapp.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.posapp.di.module.PosCacheModule;
import com.tokopedia.posapp.di.scope.PosCacheScope;
import com.tokopedia.posapp.view.service.CacheService;

import dagger.Component;

/**
 * Created by okasurya on 9/5/17.
 */

@PosCacheScope
@Component(modules = PosCacheModule.class, dependencies = AppComponent.class)
public interface PosCacheComponent {
    void inject(CacheService cacheService);
}
