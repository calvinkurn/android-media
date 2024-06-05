package com.tokopedia.media.loaderfresco.network

import okhttp3.Interceptor
import okhttp3.Response

//Adding header to okhttp interceptor
class AddHeaderInterceptor (val headerName: String, val headerValue: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader(headerName, headerValue)

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}
