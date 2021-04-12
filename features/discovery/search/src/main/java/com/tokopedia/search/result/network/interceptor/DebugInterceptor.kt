package com.tokopedia.search.result.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class DebugInterceptor internal constructor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        addDebugHeader(newRequest)
        return chain.proceed(newRequest.build())
    }

    private fun addDebugHeader(newRequest: Request.Builder) {
        newRequest.addHeader("BuildType", "DEBUG")
    }
}