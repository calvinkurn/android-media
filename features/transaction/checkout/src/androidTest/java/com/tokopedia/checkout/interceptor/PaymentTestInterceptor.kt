package com.tokopedia.checkout.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PaymentTestInterceptor : BaseCheckoutInterceptor() {

    var customGetPaymentFeeResponsePath: String? = null
    var customGetPaymentFeeThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_PAYMENT_FEE_QUERY)) {
            if (customGetPaymentFeeThrowable != null) {
                throw customGetPaymentFeeThrowable!!
            } else if (customGetPaymentFeeResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetPaymentFeeResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_PAYMENT_FEE_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetPaymentFeeResponsePath = null
        customGetPaymentFeeThrowable = null
    }
}

const val GET_PAYMENT_FEE_QUERY = "getPaymentFeeCheckout"

const val GET_PAYMENT_FEE_DEFAULT_RESPONSE_PATH = "payment/payment_fee_default_response.json"
