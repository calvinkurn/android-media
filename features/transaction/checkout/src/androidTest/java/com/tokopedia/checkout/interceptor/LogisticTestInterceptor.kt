package com.tokopedia.checkout.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.tokopedia.checkout.test.R as checkouttestR

class LogisticTestInterceptor(context: Context) : BaseCheckoutInterceptor(context) {

    var customRatesResponsePath: Int? = null
    var customRatesThrowable: IOException? = null

    var customSellyResponsePath: Int? = null
    var customSellyThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(RATES_QUERY)) {
            if (customRatesThrowable != null) {
                throw customRatesThrowable!!
            } else if (customRatesResponsePath != null) {
                return mockResponse(copy, getRawString(customRatesResponsePath!!))
            }
            return mockResponse(copy, getRawString(RATES_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(SELLY_KEY)) {
            if (customSellyThrowable != null) {
                throw customSellyThrowable!!
            } else if (customSellyResponsePath != null) {
                return mockResponse(copy, getRawString(customSellyResponsePath!!))
            }
            return mockResponse(copy, getRawString(SELLY_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customRatesResponsePath = null
        customRatesThrowable = null
    }
}

const val RATES_QUERY = "ratesV3"
const val SELLY_KEY = "ongkirGetScheduledDeliveryRates"

val RATES_DEFAULT_RESPONSE_PATH = checkouttestR.raw.ratesv3_tokonow_default_response
val SELLY_DEFAULT_RESPONSE_PATH = checkouttestR.raw.selly_default_response
