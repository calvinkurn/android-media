package com.tokopedia.autocomplete.initialstate.di

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

@InitialStateScope
@Module
class InitialStateInterceptorModule {
    @InitialStateScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
    }

    @InitialStateScope
    @Provides
    fun provideApiCacheInterceptor(@ApplicationContext context: Context): CacheApiInterceptor {
        return CacheApiInterceptor(context)
    }

    @InitialStateScope
    @Provides
    internal fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools())

        return ChuckerInterceptor(
                context, collector)
    }

    @InitialStateScope
    @Provides
    fun provideTkpdBaseInterceptor(): TkpdBaseInterceptor {
        return TkpdBaseInterceptor()
    }
}
