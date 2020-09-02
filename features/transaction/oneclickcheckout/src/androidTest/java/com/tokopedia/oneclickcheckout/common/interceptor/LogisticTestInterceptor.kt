package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LogisticTestInterceptor : BaseOccInterceptor() {

    var customRatesResponsePath: String? = null

    var customRatesThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(RATES_QUERY)) {
            if (customRatesThrowable != null) {
                throw customRatesThrowable!!
            } else if (customRatesResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customRatesResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(RATES_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }
}

const val RATES_QUERY = "ratesV3"

const val RATES_DEFAULT_RESPONSE_PATH = "logistic/rates_default_response.json"

const val RATES_WITH_INSURANCE_RESPONSE_PATH = "logistic/rates_with_insurance_response.json"