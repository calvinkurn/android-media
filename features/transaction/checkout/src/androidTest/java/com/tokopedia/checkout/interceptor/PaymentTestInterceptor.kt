package com.tokopedia.checkout.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.tokopedia.checkout.test.R as checkouttestR

class PaymentTestInterceptor(context: Context) : BaseCheckoutInterceptor(context) {

    var customGetPaymentFeeResponsePath: Int? = null
    var customGetPaymentFeeThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_PAYMENT_FEE_QUERY)) {
            if (customGetPaymentFeeThrowable != null) {
                throw customGetPaymentFeeThrowable!!
            } else if (customGetPaymentFeeResponsePath != null) {
                return mockResponse(copy, getRawString(customGetPaymentFeeResponsePath!!))
            }
            return mockResponse(copy, getRawString(GET_PAYMENT_FEE_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetPaymentFeeResponsePath = null
        customGetPaymentFeeThrowable = null
    }
}

const val GET_PAYMENT_FEE_QUERY = "getPaymentFeeCheckout"
val GET_PAYMENT_FEE_DEFAULT_RESPONSE_PATH = checkouttestR.raw.payment_fee_default_response
