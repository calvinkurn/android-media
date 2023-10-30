package com.tokopedia.common.network.cdn

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class CdnInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val start = System.currentTimeMillis()
        val response = try {
            chain.proceed(request)
        } catch (expected: Exception) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(999)
                .message(expected.localizedMessage ?: "Unknown")
                .body("{$expected}".toResponseBody())
                .build()
        }
        val end = System.currentTimeMillis()
        val cost = end - start
        CdnTracker.log(context, response, cost)
        return response
    }
}
