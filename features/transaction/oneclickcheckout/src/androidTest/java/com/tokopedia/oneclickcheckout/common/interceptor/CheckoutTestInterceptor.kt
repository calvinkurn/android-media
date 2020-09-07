package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CheckoutTestInterceptor : BaseOccInterceptor() {

    var customCheckoutResponsePath: String? = null

    var customCheckoutThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(CHECKOUT_QUERY)) {
            if (customCheckoutThrowable != null) {
                throw customCheckoutThrowable!!
            } else if (customCheckoutResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customCheckoutResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(CHECKOUT_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }
}

const val CHECKOUT_QUERY = "one_click_checkout"

const val CHECKOUT_DEFAULT_RESPONSE_PATH = "checkout/one_click_checkout_default_response.json"
const val CHECKOUT_EMPTY_STOCK_RESPONSE_PATH = "checkout/one_click_checkout_empty_stock_error_response.json"