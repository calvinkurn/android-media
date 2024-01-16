package com.tokopedia.checkout.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LogisticTestInterceptor : BaseCheckoutInterceptor() {

    var customRatesResponsePath: String? = null
    var customRatesThrowable: IOException? = null

    var customRatesResponsePathByCartStringGroup: HashMap<String, String> = HashMap()

    var customSellyResponsePath: String? = null
    var customSellyThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(RATES_QUERY)) {
            if (customRatesThrowable != null) {
                throw customRatesThrowable!!
            } else {
                for (entry in customRatesResponsePathByCartStringGroup) {
                    if (requestString.contains(entry.key)) {
                        return mockResponse(copy, getJsonFromResource(entry.value))
                    }
                }
                if (customRatesResponsePath != null) {
                    return mockResponse(copy, getJsonFromResource(customRatesResponsePath!!))
                }
            }
            return mockResponse(copy, getJsonFromResource(RATES_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(SELLY_KEY)) {
            if (customSellyThrowable != null) {
                throw customSellyThrowable!!
            } else if (customSellyResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customSellyResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(SELLY_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customRatesResponsePath = null
        customRatesThrowable = null

        customSellyResponsePath = null
        customSellyThrowable = null
    }
}

const val RATES_QUERY = "ratesV3"
const val SELLY_KEY = "ongkirGetScheduledDeliveryRates"

const val RATES_TOKONOW_DEFAULT_RESPONSE_PATH = "logistic/ratesv3_tokonow_default_response.json"
const val RATES_SELLY_DEFAULT_RESPONSE_PATH = "logistic/ratesv3_selly_default_response.json"
const val RATES_TOKONOW_WITH_ADDITIONAL_PRICE_RESPONSE_PATH = "logistic/ratesv3_tokonow_with_additional_price_response.json"
const val RATES_TOKONOW_WITH_NORMAL_PRICE_RESPONSE_PATH = "logistic/ratesv3_tokonow_with_normal_price_response.json"
const val RATES_DEFAULT_RESPONSE_PATH = "logistic/ratesv3_analytics_default_response.json"

const val SELLY_DEFAULT_RESPONSE_PATH = "logistic/selly_default_response.json"
const val SELLY_NO_PROMO_RESPONSE_PATH = "logistic/selly_no_promo_response.json"
