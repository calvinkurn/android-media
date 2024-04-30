package com.tokopedia.logisticcart.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ShippingWidgetTestInterceptor : BaseShippingWidgetInterceptor() {

    var customRatesV3Response: String? = null
    var customRatesError: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(RATES_V3_KEY)) {
            if (customRatesError != null) {
                throw customRatesError!!
            } else if (customRatesV3Response != null) {
                return mockResponse(
                    copy,
                    ResourcePathUtil.getJsonFromResource(customRatesV3Response!!)
                )
            }
            return mockResponse(
                copy,
                ResourcePathUtil.getJsonFromResource(RATES_V3_DEFAULT_RESPONSE_PATH)
            )
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customRatesV3Response = null
        customRatesError = null
    }
}

const val RATES_V3_KEY = "ratesV3"

const val RATES_V3_DEFAULT_RESPONSE_PATH = "rates_v3_default_response.json"
