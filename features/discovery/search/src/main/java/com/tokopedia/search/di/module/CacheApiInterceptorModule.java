package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class CacheApiInterceptorModule {

    @SearchScope
    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }
}
