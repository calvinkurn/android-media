package com.tokopedia.mediauploader.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.mediauploader.util.NetworkTimeOutInterceptor
import com.tokopedia.mediauploader.util.NetworkTimeOutInterceptor.Companion.DEFAULT_TIMEOUT
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module class NetworkModule {

    @Provides
    @MediaUploaderQualifier
    fun provideOkHttpClientBuilder(
            @ApplicationContext context: Context,
            @MediaUploaderQualifier userSession: UserSessionInterface
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .callTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(NetworkTimeOutInterceptor())
                .also {
                    // adding tkpdAuth and fingerprint interceptor
                    (context as? NetworkRouter?)?.let { router ->
                        it.addInterceptor(FingerprintInterceptor(router, userSession))
                        it.addInterceptor(TkpdAuthInterceptor(context, router, userSession))
                    }

                    if (GlobalConfig.isAllowDebuggingTools()) {
                        it.addInterceptor(ChuckerInterceptor(context))
                    }
                }
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson())
                )
    }

    companion object {
        private const val BASE_URL = "https://upedia.tokopedia.net/"
    }

}