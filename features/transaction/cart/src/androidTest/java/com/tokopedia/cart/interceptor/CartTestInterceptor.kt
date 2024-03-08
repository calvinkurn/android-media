package com.tokopedia.cart.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CartTestInterceptor : BaseCartInterceptor() {

    var customGetCartResponsePath: String? = null
    var customGetCartThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(CART_REVAMP_KEY)) {
            if (customGetCartThrowable != null) {
                throw customGetCartThrowable!!
            } else if (customGetCartResponsePath != null) {
                return mockResponse(
                    copy,
                    ResourcePathUtil.getJsonFromResource(customGetCartResponsePath!!)
                )
            }
            return mockResponse(
                copy,
                ResourcePathUtil.getJsonFromResource(CART_DEFAULT_RESPONSE_PATH)
            )
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetCartResponsePath = null
        customGetCartThrowable = null
    }
}

const val CART_REVAMP_KEY = "cartRevampV4"

const val CART_DEFAULT_RESPONSE_PATH = "cart/cart_bundle_happy_flow_response.json"
const val CART_GWP_RESPONSE_PATH = "cart/cart_gwp_response.json"
