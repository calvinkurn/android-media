package com.tokopedia.autocomplete.initialstate.di

import android.content.Context

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.interceptor.TkpdBaseInterceptor

import com.tokopedia.user.session.UserSessionInterface

import dagger.Module
import dagger.Provides

@Module
class InitialStateInterceptorModule {
    @InitialStateScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
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

    @InitialStateScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @InitialStateScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }
}
