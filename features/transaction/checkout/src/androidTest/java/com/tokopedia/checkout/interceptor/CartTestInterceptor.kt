package com.tokopedia.checkout.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.tokopedia.checkout.test.R as checkouttestR

class CartTestInterceptor(context: Context) : BaseCheckoutInterceptor(context) {

    var customSafResponsePath: Int? = null
    var customSafThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(SHIPMENT_ADDRESS_FORM_KEY)) {
            if (customSafThrowable != null) {
                throw customSafThrowable!!
            } else if (customSafResponsePath != null) {
                return mockResponse(copy, getRawString(customSafResponsePath!!))
            }
            return mockResponse(copy, getRawString(SAF_TOKONOW_DEFAULT_RESPONSE))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customSafResponsePath = null
        customSafThrowable = null
    }
}

const val SHIPMENT_ADDRESS_FORM_KEY = "shipmentAddressFormV4"
val SAF_TOKONOW_DEFAULT_RESPONSE = checkouttestR.raw.saf_bundle_tokonow_default_response
