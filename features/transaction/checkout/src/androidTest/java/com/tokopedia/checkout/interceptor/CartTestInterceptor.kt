package com.tokopedia.checkout.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CartTestInterceptor : BaseCheckoutInterceptor() {

    var customSafResponsePath: String? = null
    var customSafThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(SHIPMENT_ADDRESS_FORM_KEY)) {
            if (customSafThrowable != null) {
                throw customSafThrowable!!
            } else if (customSafResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customSafResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(SAF_TOKONOW_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(SAVE_SHIPMENT_KEY)) {
            return mockResponse(copy, getJsonFromResource(SAVE_SHIPMENT_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customSafResponsePath = null
        customSafThrowable = null
    }
}

const val SHIPMENT_ADDRESS_FORM_KEY = "shipmentAddressFormV4"
const val SAVE_SHIPMENT_KEY = "save_shipment"

const val SAF_TOKONOW_DEFAULT_RESPONSE_PATH = "cart/saf_bundle_tokonow_default_response.json"
const val SAF_TOKONOW_WITH_FAILED_DEFAULT_DURATION_RESPONSE_PATH = "cart/saf_bundle_tokonow_with_failed_default_duration_response.json"
const val SAF_TOKONOW_SELLY_RESPONSE_PATH = "cart/saf_tokonow_selly_response.json"
const val SAF_OWOC_DEFAULT_RESPONSE_PATH = "cart/saf_owoc_default_response.json"

const val SAVE_SHIPMENT_DEFAULT_RESPONSE_PATH = "cart/save_shipment_default_response.json"
