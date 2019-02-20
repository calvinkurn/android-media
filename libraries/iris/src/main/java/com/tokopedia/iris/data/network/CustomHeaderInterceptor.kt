package com.tokopedia.iris.data.network

import com.tokopedia.iris.*
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by meta on 20/02/19.
 */
class CustomHeaderInterceptor(val session: Session) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
        request.header(HEADER_CONTENT_TYPE, HEADER_JSON)
        if (!session.getUserId().isBlank()) {
            request.header(HEADER_USER_ID, session.getUserId())
        }
        request.header(HEADER_DEVICE, HEADER_ANDROID)
        request.method(original.method(), original.body())

        return try {
            chain.proceed(request.build())
        } catch (e: IOException) {
            chain.proceed(original.newBuilder().build())
        }
    }
}