package com.tokopedia.media.loader.module.interceptor

import android.content.Context
import com.tokopedia.media.loader.internal.NetworkResponseManager
import okhttp3.Interceptor
import okhttp3.Response

class NetworkLogInterceptor constructor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val headers = response.headers

        try {
            NetworkResponseManager
                .getInstance(context)
                .set(headers)
        } catch (ignored: Throwable) {}

        return response
    }
}
