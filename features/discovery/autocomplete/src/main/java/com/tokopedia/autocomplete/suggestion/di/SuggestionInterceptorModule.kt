package com.tokopedia.autocomplete.suggestion.di

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
class SuggestionInterceptorModule {
    @SuggestionScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
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

    @SuggestionScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @SuggestionScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }
}
