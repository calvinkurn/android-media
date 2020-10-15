package com.tokopedia.media.loader.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient

object GlideNetworkInterceptor {

    fun okHttpInterceptor(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain: Interceptor.Chain? ->
            chain?.proceed(chain.request())
        }

        return httpClient.build()
    }

}