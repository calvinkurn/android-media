package com.tokopedia.favorite.di.modul

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.favorite.di.scope.FavoriteScope
import dagger.Module
import dagger.Provides

@FavoriteScope
@Module
class CacheApiInterceptorModule {

    @FavoriteScope
    @Provides
    fun provideCacheApiInterceptor(@ApplicationContext context: Context): CacheApiInterceptor {
        return CacheApiInterceptor(context)
    }

}
