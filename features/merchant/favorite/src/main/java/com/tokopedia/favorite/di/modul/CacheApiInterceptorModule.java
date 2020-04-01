package com.tokopedia.favorite.di.modul;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.favorite.di.scope.FavoriteScope;

import dagger.Module;
import dagger.Provides;

@FavoriteScope
@Module
public class CacheApiInterceptorModule {

    @FavoriteScope
    @Provides
    public CacheApiInterceptor provideCacheApiInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context);
    }
}
