package com.tokopedia.common.network.cdn

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class CdnInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val response = chain.proceed(request)
            CdnTracker.log(context, response)
            response
        } catch (expected: Exception) {
            val response = Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(999)
                .message(expected.localizedMessage ?: "Unknown")
                .body("{$expected}".toResponseBody())
                .build()
            CdnTracker.log(context, response)
            response
        }
    }
}
