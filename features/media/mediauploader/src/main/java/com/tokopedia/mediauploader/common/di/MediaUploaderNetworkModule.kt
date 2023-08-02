package com.tokopedia.mediauploader.common.di

import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class MediaUploaderNetworkModule {

    @Provides
    @UploaderQualifier
    fun provideMediaUploaderRetrofit(
        @UploaderQualifier retrofit: Retrofit.Builder,
        @UploaderQualifier okHttpClient: OkHttpClient.Builder
    ): Retrofit {
        return retrofit.client(okHttpClient.build()).build()
    }

    @Provides
    fun provideImageUploaderServices(
        @UploaderQualifier retrofit: Retrofit
    ): ImageUploadServices {
        return retrofit.create(ImageUploadServices::class.java)
    }

    @Provides
    fun provideVideoUploaderServices(
        @UploaderQualifier retrofit: Retrofit
    ): VideoUploadServices {
        return retrofit.create(VideoUploadServices::class.java)
    }

}
