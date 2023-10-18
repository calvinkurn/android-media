package com.tokopedia.checkout.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.tokopedia.checkout.test.R as checkouttestR

class PromoTestInterceptor(context: Context) : BaseCheckoutInterceptor(context) {

    var customValidateUseResponsePath: Int? = null
    var customValidateUseThrowable: IOException? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(VALIDATE_USE_PROMO_REVAMP_QUERY)) {
            if (customValidateUseThrowable != null) {
                throw customValidateUseThrowable!!
            } else if (customValidateUseResponsePath != null) {
                return mockResponse(copy, getRawString(customValidateUseResponsePath!!))
            }
            return mockResponse(copy, getRawString(VALIDATE_USE_PROMO_REVAMP_DEFAULT_RESPONSE))
        }
        if (requestString.contains(CLEAR_PROMO_QUERY)) {
            return mockResponse(copy, getRawString(CLEAR_PROMO_DEFAULT_RESPONSE))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customValidateUseResponsePath = null
        customValidateUseThrowable = null
    }
}

const val VALIDATE_USE_PROMO_REVAMP_QUERY = "validate_use_promo_revamp"
const val CLEAR_PROMO_QUERY = "clearCacheAutoApplyStack"

val VALIDATE_USE_PROMO_REVAMP_DEFAULT_RESPONSE = checkouttestR.raw.validate_use_tokonow_default_response
val CLEAR_PROMO_DEFAULT_RESPONSE = checkouttestR.raw.clear_promo_default_response
