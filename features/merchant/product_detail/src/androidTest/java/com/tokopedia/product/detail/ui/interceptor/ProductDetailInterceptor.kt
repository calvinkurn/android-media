package com.tokopedia.product.detail.ui.interceptor

import com.tokopedia.product.detail.util.ResponseUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Yehezkiel on 29/07/21
 */

class ProductDetailInterceptor : BasePdpInterceptor() {

    var customMiniCartResponsePath: String? = null
    var customP1ResponsePath: String? = null
    var customP2DataResponsePath: String? = null
    var customP2ErrorResponsePath: String? = null
    var customAtcV2ResponsePath: String? = null
    var customTickerResponsePath: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_MINI_CART) && customMiniCartResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customMiniCartResponsePath!!))
        }
        if (requestString.contains(GET_PDP_P1) && customP1ResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customP1ResponsePath!!))
        }

        if (requestString.contains(GET_PDP_P2_DATA)) {
            return if (customP2ErrorResponsePath != null) {
                mockResponse(copy, "force error")
            } else {
                mockResponse(copy, getJsonFromResource(customP2DataResponsePath!!))
            }
        }

        if (requestString.contains(GET_SUCCESS_ATC) && customAtcV2ResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customAtcV2ResponsePath!!))
        }

        if (requestString.contains(GET_TICKER) && customTickerResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customTickerResponsePath!!))
        }

        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customMiniCartResponsePath = null
        customP1ResponsePath = null
        customP2DataResponsePath = null
        customP2ErrorResponsePath = null
        customAtcV2ResponsePath = null
        customTickerResponsePath = null
    }
}

const val GET_MINI_CART = "mini_cart"
const val GET_PDP_P1 = "pdpGetLayout"
const val GET_PDP_P2_DATA = "GetPdpGetData"
const val GET_SUCCESS_ATC = "add_to_cart_v2"
const val GET_TICKER = "get_ticker"
