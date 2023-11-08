package com.tokopedia.checkout.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CheckoutTestInterceptor : BaseCheckoutInterceptor() {

    var customCheckoutResponsePath: String? = null
    var customCheckoutThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(CHECKOUT_QUERY)) {
            if (customCheckoutThrowable != null) {
                throw customCheckoutThrowable!!
            } else if (customCheckoutResponsePath != null) {
                return mockResponse(
                    copy,
                    getJsonFromResource(customCheckoutResponsePath!!)
                )
            }
            return mockResponse(copy, getJsonFromResource(CHECKOUT_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customCheckoutResponsePath = null
        customCheckoutThrowable = null
    }
}

const val CHECKOUT_QUERY = "checkout"

const val CHECKOUT_DEFAULT_RESPONSE_PATH = "checkout/checkout_analytics_default_response.json"
