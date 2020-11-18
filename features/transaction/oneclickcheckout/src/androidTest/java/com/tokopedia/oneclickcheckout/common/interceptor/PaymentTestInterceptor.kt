package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PaymentTestInterceptor : BaseOccInterceptor() {

    var customGetListingParamResponsePath: String? = null
    var customGetListingParamThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_LISTING_PARAM_QUERY)) {
            if (customGetListingParamThrowable != null) {
                throw customGetListingParamThrowable!!
            } else if (customGetListingParamResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetListingParamResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_LISTING_PARAM_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(GET_OVO_TOP_UP_URL_QUERY)) {
            return mockResponse(copy, getJsonFromResource(GET_LISTING_PARAM_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetListingParamResponsePath = null
        customGetListingParamThrowable = null
    }
}

const val GET_LISTING_PARAM_QUERY = "getListingParams"

const val GET_LISTING_PARAM_DEFAULT_RESPONSE_PATH = "payment/get_payment_listing_default_response.json"

const val GET_OVO_TOP_UP_URL_QUERY = "fetchInstantTopupURL"

const val GET_OVO_TOP_UP_URL_DEFAULT_RESPONSE_PATH = "payment/get_ovo_top_up_url_default_response.json"