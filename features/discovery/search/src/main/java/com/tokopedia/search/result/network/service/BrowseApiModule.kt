package com.tokopedia.search.result.network.service

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdBaseInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.search.di.module.FingerprintInterceptorModule
import com.tokopedia.search.di.module.SearchRetrofitBuilderModule
import com.tokopedia.search.di.module.TkpdBaseInterceptorModule
import com.tokopedia.search.di.qualifier.AceQualifier
import com.tokopedia.search.di.qualifier.SearchQualifier
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.network.interceptor.DebugInterceptor
import com.tokopedia.search.result.network.interceptor.DebugInterceptorModule
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptor
import com.tokopedia.search.result.network.interceptor.TopAdsAuthInterceptorModule
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module(includes = [SearchRetrofitBuilderModule::class, TopAdsAuthInterceptorModule::class, FingerprintInterceptorModule::class, TkpdBaseInterceptorModule::class, DebugInterceptorModule::class])
class BrowseApiModule {
    @SearchScope
    @Provides
    @AceQualifier
    fun provideAceService(@AceQualifier retrofit: Retrofit): BrowseApi {
        return retrofit.create(BrowseApi::class.java)
    }

    @SearchScope
    @Provides
    @AceQualifier
    fun provideAceRetrofit(
            @AceQualifier okHttpClient: OkHttpClient?,
            @SearchQualifier retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(SearchConstant.BaseUrl.ACE_DOMAIN).client(okHttpClient).build()
    }

    @SearchScope
    @Provides
    @AceQualifier
    fun provideOkHttpClientAceQualifier(
            @ApplicationContext context: Context?,
            topAdsAuthInterceptor: TopAdsAuthInterceptor?,
            fingerprintInterceptor: FingerprintInterceptor?,
            tkpdBaseInterceptor: TkpdBaseInterceptor?,
            debugInterceptor: DebugInterceptor?,
            httpLoggingInterceptor: HttpLoggingInterceptor?
    ): OkHttpClient {
        val client = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
                .addInterceptor(httpLoggingInterceptor)
                .build()
        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, client.newBuilder())
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdBaseInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            tkpdOkHttpBuilder.addInterceptor(debugInterceptor)
        }
        return tkpdOkHttpBuilder.build()
    }
}