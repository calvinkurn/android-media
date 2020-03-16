package com.tokopedia.autocomplete.suggestion.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdBaseInterceptor
import dagger.Module
import dagger.Provides

@SuggestionScope
@Module
class SuggestionInterceptorModule {
    @SuggestionScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
    }

    @SuggestionScope
    @Provides
    fun provideApiCacheInterceptor(@ApplicationContext context: Context): CacheApiInterceptor {
        return CacheApiInterceptor(context)
    }

    @SuggestionScope
    @Provides
    internal fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools())

        return ChuckerInterceptor(
                context, collector)
    }

    @SuggestionScope
    @Provides
    fun provideTkpdBaseInterceptor(): TkpdBaseInterceptor {
        return TkpdBaseInterceptor()
    }
}
