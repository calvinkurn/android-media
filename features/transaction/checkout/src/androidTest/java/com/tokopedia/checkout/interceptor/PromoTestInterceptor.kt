package com.tokopedia.checkout.interceptor

import com.tokopedia.test.application.util.ResourcePathUtil.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PromoTestInterceptor : BaseCheckoutInterceptor() {

    var customValidateUseResponsePath: String? = null
    var customValidateUseThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(VALIDATE_USE_PROMO_REVAMP_QUERY)) {
            if (customValidateUseThrowable != null) {
                throw customValidateUseThrowable!!
            } else if (customValidateUseResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customValidateUseResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(VALIDATE_USE_NO_PROMO_RESPONSE))
        }
        if (requestString.contains(CLEAR_PROMO_QUERY)) {
            return mockResponse(copy, getJsonFromResource(CLEAR_PROMO_DEFAULT_RESPONSE))
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

const val VALIDATE_USE_TOKONOW_DEFAULT_RESPONSE = "promo/validate_use_tokonow_default_response.json"
const val VALIDATE_USE_SELLY_DEFAULT_RESPONSE = "promo/validate_use_selly_default_response.json"
const val VALIDATE_USE_SELLY_2HR_RESPONSE = "promo/validate_use_selly_2hr_response.json"
const val VALIDATE_USE_NO_PROMO_RESPONSE = "promo/validate_use_with_no_promo_response.json"

const val CLEAR_PROMO_DEFAULT_RESPONSE = "promo/clear_promo_default_response.json"
