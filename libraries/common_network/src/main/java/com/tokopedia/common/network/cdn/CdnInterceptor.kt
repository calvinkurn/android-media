package com.tokopedia.common.network.cdn

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class CdnInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            CdnTracker.succeed(context, response)
        } catch (expected: Exception) {
            CdnTracker.failed(context, response, expected)
        }
        return response
    }
}
