package com.tokopedia.media.loader.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import timber.log.Timber

object GlideNetworkInterceptor {

    fun okHttpInterceptor(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain: Interceptor.Chain? ->
            val response = chain?.proceed(chain.request())
            val header = response?.header(HEADERS_ECT)

            Timber.d("Connection-Type (ECT):$header")

            response
        }

        return httpClient.build()
    }

}