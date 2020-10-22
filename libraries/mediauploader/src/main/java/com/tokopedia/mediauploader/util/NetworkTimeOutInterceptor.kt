package com.tokopedia.mediauploader.util

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkTimeOutInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // get timeout data
        val timeOut = request.header(HEADER_TIMEOUT)
        val requestTimeout = timeOut?.toInt()?: DEFAULT_TIMEOUT

        return chain
                .withConnectTimeout(requestTimeout, TimeUnit.SECONDS)
                .withWriteTimeout(requestTimeout, TimeUnit.SECONDS)
                .withReadTimeout(requestTimeout, TimeUnit.SECONDS)
                .proceed(request)
    }

    companion object {
        const val HEADER_TIMEOUT = "TIME_OUT"
        const val DEFAULT_TIMEOUT = 120
    }

}