package com.tokopedia.pdpCheckout.testing.product.detail

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Yehezkiel on 29/07/21
 */

class ProductDetailInterceptor : BasePdpInterceptor() {

    var customP1ResponsePath: String? = null
    var customP2DataResponsePath: String? = null
    var customRecomWidgetRecomAtcResponsePath: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_PDP_P1) && customP1ResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customP1ResponsePath!!))
        } else if (requestString.contains(GET_PDP_P2_DATA)) {
            return mockResponse(copy, getJsonFromResource(customP2DataResponsePath!!))
        } else if (requestString.contains(GET_RECOM_AFTER_ATC) && customRecomWidgetRecomAtcResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customRecomWidgetRecomAtcResponsePath!!))
        } else if (!(
            requestString.contains("\"cart\"") ||
                requestString.contains("\"cart_recent_view\"") ||
                requestString.contains("update_cart_counter")
            )
        ) {
            return mockResponse(copy, "")
        }

        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customP1ResponsePath = null
        customP2DataResponsePath = null
        customRecomWidgetRecomAtcResponsePath = null
    }
}

const val GET_PDP_P1 = "pdpGetLayout"
const val GET_PDP_P2_DATA = "GetPdpGetData"
const val GET_RECOM_AFTER_ATC = "productRecommendation"

const val RESPONSE_P1_PATH = "raw/response_mock_p1_test.json"
const val RESPONSE_P2_DATA_PATH = "raw/response_mock_p2_ui_test.json"
const val RESPONSE_RECOM_AFTER_ATC_PATH = "raw/response_recom_after_atc_test.json"
