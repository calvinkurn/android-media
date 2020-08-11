package com.tokopedia.mediauploader.di

import com.google.gson.Gson
import com.tokopedia.mediauploader.util.NetworkTimeOutInterceptor
import com.tokopedia.mediauploader.util.NetworkTimeOutInterceptor.Companion.DEFAULT_TIMEOUT
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module class NetworkModule {

    @Provides
    @MediaUploaderQualifier
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .callTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(NetworkTimeOutInterceptor())
                .retryOnConnectionFailure(false)
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