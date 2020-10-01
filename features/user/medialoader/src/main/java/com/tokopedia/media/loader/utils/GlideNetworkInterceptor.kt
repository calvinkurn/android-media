package com.tokopedia.media.loader.utils

import com.tokopedia.media.loader.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import timber.log.Timber

object GlideNetworkInterceptor {

    fun okHttpInterceptor(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain: Interceptor.Chain? ->
            val response = chain?.proceed(chain.request())
            val header = response?.header(HEADER_ECT)

            // send to analytics
            Timber.d("ECT#$header")

            // showing into logcat tool
            if (BuildConfig.DEBUG) {
                println(response?.headers().toString())
            }

            response
        }

        return httpClient.build()
    }

}