package com.tokopedia.profile.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.profile.data.interceptor.TopAdsAuthInterceptor
import com.tokopedia.profile.data.network.TOPADS_BASE_URL
import com.tokopedia.profile.data.network.TopAdsApi
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author by milhamj on 10/17/18.
 */
@Module
class ProfileNetworkModule {
    @ProfileScope
    @TopAdsQualifier
    @Provides
    fun provideBaseUrl(): String {
        return TOPADS_BASE_URL
    }

    @ProfileScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }
        return context as NetworkRouter
    }

    @ProfileScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSession): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @ProfileScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter,
                                      userSession: UserSession): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @ProfileScope
    @Provides
    fun provideStringResponseConverter(): StringResponseConverter {
        return StringResponseConverter()
    }

    @ProfileScope
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @ProfileScope
    @Provides
    fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
    }

    @ProfileScope
    @Provides
    fun provideTopAdsAuthInterceptor(@ApplicationContext context: Context,
                                     networkRouter: NetworkRouter,
                                     userSession: UserSession): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(context, networkRouter, userSession)
    }

    @ProfileScope
    @Provides
    fun provideTkpdOkHttpBuilder(@ApplicationContext context: Context,
                                 okHttpBuilder: OkHttpClient.Builder,
                                 topAdsAuthInterceptor: TopAdsAuthInterceptor): TkpdOkHttpBuilder {
        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, okHttpBuilder)
        tkpdOkHttpBuilder.addInterceptor(topAdsAuthInterceptor)
        return tkpdOkHttpBuilder
    }

    @ProfileScope
    @Provides
    fun provideRetrofit(@TopAdsQualifier baseUrl: String, tkpdOkHttpBuilder: TkpdOkHttpBuilder,
                        tkpdAuthInterceptor: TkpdAuthInterceptor,
                        fingerprintInterceptor: FingerprintInterceptor,
                        stringResponseConverter: StringResponseConverter,
                        gsonBuilder: GsonBuilder): Retrofit {
        return CommonNetwork.createRetrofit(baseUrl, tkpdOkHttpBuilder, tkpdAuthInterceptor,
                fingerprintInterceptor, stringResponseConverter, gsonBuilder)
    }

    @ProfileScope
    @Provides
    fun provideTopAdsApi(retrofit: Retrofit): TopAdsApi {
        return retrofit.create(TopAdsApi::class.java)
    }
}