package com.tokopedia.mediauploader.util

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkTimeOutInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // get timeOut data
        val timeOut = request.header(HEADER_TIMEOUT)

        // replace
        val connectTimeout = timeOut?.toInt()?: DEFAULT_TIMEOUT
        val writeTimeout = timeOut?.toInt()?: DEFAULT_TIMEOUT
        val readTimeout = timeOut?.toInt()?: DEFAULT_TIMEOUT

        return chain
                .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .proceed(request)
    }

    companion object {
        const val HEADER_TIMEOUT = "TIME_OUT"
        const val DEFAULT_TIMEOUT = 60
    }

}