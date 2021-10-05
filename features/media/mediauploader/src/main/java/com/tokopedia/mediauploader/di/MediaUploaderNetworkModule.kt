package com.tokopedia.mediauploader.di

import com.tokopedia.mediauploader.data.ImageUploadServices
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module (includes = [NetworkModule::class])
class MediaUploaderNetworkModule {

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderRetrofit(
        @MediaUploaderQualifier retrofit: Retrofit.Builder,
        @MediaUploaderQualifier okHttpClient: OkHttpClient.Builder
    ): Retrofit {
        return retrofit.client(okHttpClient.build()).build()
    }

    @Provides
    fun provideMediaUploaderServices(
        @MediaUploaderQualifier retrofit: Retrofit
    ): ImageUploadServices {
        return retrofit.create(ImageUploadServices::class.java)
    }

}