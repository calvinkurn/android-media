package com.tokopedia.posapp.cache.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.cache.view.service.CacheService;

import dagger.Component;

/**
 * Created by okasurya on 9/5/17.
 */

@PosCacheScope
@Component(modules = PosCacheModule.class, dependencies = BaseAppComponent.class)
public interface PosCacheComponent {
    void inject(CacheService cacheService);
}
