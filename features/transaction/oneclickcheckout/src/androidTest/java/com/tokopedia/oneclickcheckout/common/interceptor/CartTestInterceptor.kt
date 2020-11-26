package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CartTestInterceptor : BaseOccInterceptor() {

    var customGetOccCartResponsePath: String? = null
    var customGetOccCartThrowable: IOException? = null

    var customUpdateCartOccResponsePath: String? = null
    var customUpdateCartOccThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_OCC_CART_QUERY)) {
            if (customGetOccCartThrowable != null) {
                throw customGetOccCartThrowable!!
            } else if (customGetOccCartResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetOccCartResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_OCC_CART_PAGE_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(UPDATE_CART_OCC_QUERY)) {
            if (customUpdateCartOccThrowable != null) {
                throw customUpdateCartOccThrowable!!
            } else if (customUpdateCartOccResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customUpdateCartOccResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(UPDATE_CART_OCC_SUCCESS_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetOccCartResponsePath = null
        customGetOccCartThrowable = null
        customUpdateCartOccResponsePath = null
        customUpdateCartOccThrowable = null
    }
}

const val GET_OCC_CART_QUERY = "get_occ_cart_page"
const val UPDATE_CART_OCC_QUERY = "update_cart_occ"

const val GET_OCC_CART_PAGE_DEFAULT_RESPONSE_PATH = "cart/get_occ_cart_page_default_response.json"
const val GET_OCC_CART_PAGE_NO_PROFILE_RESPONSE_PATH = "cart/get_occ_cart_page_no_profile_response.json"

const val UPDATE_CART_OCC_SUCCESS_RESPONSE_PATH = "cart/update_cart_occ_success_response.json"