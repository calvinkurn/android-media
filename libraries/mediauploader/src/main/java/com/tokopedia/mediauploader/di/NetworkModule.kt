package com.tokopedia.mediauploader.di

import com.google.gson.Gson
import com.tokopedia.resources.common.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module class NetworkModule {

    @Provides
    @MediaUploaderQualifier
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(NET_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NET_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NET_WRITE_TIMEOUT, TimeUnit.SECONDS)
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
        private const val NET_READ_TIMEOUT = 60L
        private const val NET_WRITE_TIMEOUT = 60L
        private const val NET_CONNECT_TIMEOUT = 60L

        private const val BASE_URL = "https://upedia.tokopedia.net/"
    }

}