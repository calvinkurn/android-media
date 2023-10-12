package com.tokopedia.shop_nib.di.module

import com.tokopedia.shop_nib.data.service.UploadFileService
import com.tokopedia.shop_nib.di.scope.ShopNibScope
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ShopNibNetworkModule {

    @Provides
    @ShopNibScope
    fun provideOkHttpClient(@ShopNibScope loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(loggingInterceptor)
        return client.build()
    }

    @Provides
    @ShopNibScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = if (TokopediaUrl.getInstance().TYPE == Env.LIVE) {
            "https://api.tokopedia.com/merchant/toko/"
        } else {
            "https://api-staging.tokopedia.com/merchant/toko/"
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @ShopNibScope
    fun provideUploadFileService(retrofit: Retrofit) : UploadFileService = retrofit.create(UploadFileService::class.java)
}
