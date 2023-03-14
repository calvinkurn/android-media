package com.tokopedia.mvc.di.module

import com.tokopedia.mvc.common.util.UrlConstant.IMAGE_GENERATOR_PROD_URL
import com.tokopedia.mvc.common.util.UrlConstant.IMAGE_GENERATOR_STAGING_URL
import com.tokopedia.mvc.data.service.ImageGeneratorService
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ImageGeneratorModule {

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(loggingInterceptor)
        return client.build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = if (TokopediaUrl.getInstance().TYPE == Env.LIVE) {
            IMAGE_GENERATOR_PROD_URL
        } else {
            IMAGE_GENERATOR_STAGING_URL
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideImageGeneratorService(retrofit: Retrofit): ImageGeneratorService =
        retrofit.create(ImageGeneratorService::class.java)

}
