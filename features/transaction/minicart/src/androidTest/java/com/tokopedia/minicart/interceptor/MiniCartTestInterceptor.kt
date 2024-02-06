package com.tokopedia.minicart.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class MiniCartTestInterceptor : BaseMiniCartInterceptor() {

    var customMiniCartResponsePath: String? = null
    var customMiniCartThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(MINI_CART_KEY)) {
            if (customMiniCartThrowable != null) {
                throw customMiniCartThrowable!!
            } else if (customMiniCartResponsePath != null) {
                return mockResponse(
                    copy,
                    ResourcePathUtil.getJsonFromResource(customMiniCartResponsePath!!)
                )
            }
            return mockResponse(
                copy,
                ResourcePathUtil.getJsonFromResource(MINI_CART_DEFAULT_RESPONSE_PATH)
            )
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customMiniCartResponsePath = null
        customMiniCartThrowable = null
    }
}

const val MINI_CART_KEY = "mini_cart_v3"

const val MINI_CART_DEFAULT_RESPONSE_PATH = "mini_cart_default_response.json"
