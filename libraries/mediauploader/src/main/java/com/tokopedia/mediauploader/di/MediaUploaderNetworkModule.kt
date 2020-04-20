package com.tokopedia.mediauploader.di

import com.tokopedia.mediauploader.data.UploaderServices
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module class MediaUploaderNetworkModule {

    @Provides
    fun provideMediaUploaderRetrofit(
            @MediaUploaderQualifier retrofit: Retrofit.Builder,
            @MediaUploaderQualifier okHttpClient: OkHttpClient.Builder
    ): Retrofit {
        return retrofit.client(okHttpClient.build()).build()
    }

    @Provides
    fun provideMediaUploaderServices(retrofit: Retrofit): UploaderServices {
        return retrofit.create(UploaderServices::class.java)
    }

}