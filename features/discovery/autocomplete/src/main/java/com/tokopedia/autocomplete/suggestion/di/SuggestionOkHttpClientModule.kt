package com.tokopedia.autocomplete.suggestion.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module(includes = [SuggestionInterceptorModule::class])
class SuggestionOkHttpClientModule {
    @SuggestionScope
    @SuggestionNoAuth
    @Provides
    fun provideOkHttpClientNoAuth(okHttpRetryPolicy: OkHttpRetryPolicy,
                                  chuckInterceptor: ChuckerInterceptor,
                                  debugInterceptor: DebugInterceptor,
                                  tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {

        val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(debugInterceptor)
            clientBuilder.addInterceptor(chuckInterceptor)
        }
        return clientBuilder.build()
    }

    @SuggestionScope
    @Provides
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }
}