package com.tokopedia.posapp.cache.di;

import com.tokopedia.posapp.cache.view.service.CacheService;
import com.tokopedia.posapp.di.component.PosAppComponent;

import dagger.Component;

/**
 * Created by okasurya on 9/5/17.
 */

@PosCacheScope
@Component(modules = PosCacheModule.class, dependencies = PosAppComponent.class)
public interface PosCacheComponent {
    void inject(CacheService cacheService);
}
