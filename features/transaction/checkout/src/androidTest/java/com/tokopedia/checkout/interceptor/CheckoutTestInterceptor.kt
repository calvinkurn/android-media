package com.tokopedia.checkout.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.tokopedia.checkout.test.R as checkouttestR

class CheckoutTestInterceptor(context: Context) : BaseCheckoutInterceptor(context) {

    var customCheckoutResponsePath: Int? = null
    var customCheckoutThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(CHECKOUT_QUERY)) {
            if (customCheckoutThrowable != null) {
                throw customCheckoutThrowable!!
            } else if (customCheckoutResponsePath != null) {
                return mockResponse(copy, getRawString(customCheckoutResponsePath!!))
            }
            return mockResponse(copy, getRawString(CHECKOUT_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customCheckoutResponsePath = null
        customCheckoutThrowable = null
    }
}

const val CHECKOUT_QUERY = "checkout"

val CHECKOUT_DEFAULT_RESPONSE_PATH = checkouttestR.raw.checkout_analytics_default_response
