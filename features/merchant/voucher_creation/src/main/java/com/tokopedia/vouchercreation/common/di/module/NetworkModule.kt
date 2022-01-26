package com.tokopedia.vouchercreation.common.di.module

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.product.create.data.service.ImageGeneratorService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    @VoucherCreationScope
    fun provideOkHttpClient(@VoucherCreationScope loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(loggingInterceptor)
        return client.build()
    }

    @Provides
    @VoucherCreationScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = if (TokopediaUrl.getInstance().TYPE == Env.LIVE) {
            "https://imagenerator.tokopedia.net/"
        } else {
            "https://imagenerator-staging.tokopedia.net/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @VoucherCreationScope
    fun provideImageGeneratorService(retrofit: Retrofit) : ImageGeneratorService =
        retrofit.create(ImageGeneratorService::class.java)

}