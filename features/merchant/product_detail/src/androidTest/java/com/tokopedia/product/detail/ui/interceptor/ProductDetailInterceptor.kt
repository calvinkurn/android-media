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
    var customRecomResponsePath: String? = null
    var customDiscussionResponsePath: String? = null

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

        if (requestString.contains(GET_DISCUSSION) && customDiscussionResponsePath != null) {
            return mockResponse(copy, getJsonFromResource(customDiscussionResponsePath!!))
        }

        if (requestString.contains(GET_RECOM)) {
            val mock = if (customRecomResponsePath == null) {
                ""
            } else {
                getJsonFromResource(customRecomResponsePath!!)
            }
            return mockResponse(copy, mock)
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
const val GET_RECOM = "productRecommendation"
const val GET_DISCUSSION = "discussionMostHelpful"

const val RESPONSE_P1_PATH = "raw/response_mock_p1_test.json"
const val RESPONSE_P2_DATA_PATH = "raw/response_mock_p2_ui_test.json"
const val RESPONSE_TICKER_PATH = "raw/response_get_ticker_sticky_login.json"
const val RESPONSE_DISCUSSION_MOSTHELPFUL = "raw/response_mock_discussion_most_helpful.json"

const val RESPONSE_MINICART_EMPTY_PATH = "raw/response_mock_mini_cart_empty.json"
const val RESPONSE_MINICART_PATH = "raw/response_mock_mini_cart_non_var.json"

const val RESPONSE_P1_NEGATIVE_CASE_PATH = "raw/response_mock_p1_negative_case.json"
const val RESPONSE_P2_DATA_NEGATIVE_CASE_PATH = "raw/response_mock_p2_negative_case.json"

const val RESPONSE_P1_VARIANT_TOKONOW_PATH = "raw/response_mock_p1_tokonow_variant.json"
const val RESPONSE_P2_DATA_VARIANT_TOKONOW_PATH = "raw/response_mock_p2_tokonow_variant.json"

const val RESPONSE_P1_NON_VARIANT_TOKONOW_PATH = "raw/response_mock_p1_tokonow_nonvar.json"
const val RESPONSE_P2_DATA_NON_VARIANT_TOKONOW_PATH = "raw/response_mock_p2_tokonow_nonvar.json"

const val RESPONSE_SUCCESS_ATC_NON_VARIANT_TOKONOW_PATH = "raw/response_mock_success_atc_tokonow_nonvar.json"
