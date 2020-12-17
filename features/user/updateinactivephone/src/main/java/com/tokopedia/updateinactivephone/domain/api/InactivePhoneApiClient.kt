package com.tokopedia.updateinactivephone.domain.api

import com.tokopedia.config.GlobalConfig
import com.tokopedia.url.TokopediaUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InactivePhoneApiClient<T> (
        private val service: Class<T>
) {

    private fun retrofitBuilder(): Retrofit {
        return Retrofit.Builder()
                .client(getClient())
                .baseUrl(TokopediaUrl.getInstance().ACCOUNTS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun call(): T {
        return retrofitBuilder().create(service)
    }

    private fun getClient(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
        if (GlobalConfig.DEBUG) {
            httpClient.addInterceptor(loggingInterceptor())
        }

        return httpClient.build()
    }

    private fun loggingInterceptor(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }
}