package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Provides
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import dagger.Module

@Module
class CacheApiInterceptorModule {
    @SearchScope
    @Provides
    fun provideCacheApiInterceptor(@ApplicationContext context: Context?): CacheApiInterceptor {
        return CacheApiInterceptor(context)
    }
}