package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PromoTestInterceptor : BaseOccInterceptor() {

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
            return mockResponse(copy, getJsonFromResource(VALIDATE_USE_PROMO_REVAMP_DEFAULT_RESPONSE))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customValidateUseResponsePath = null
        customValidateUseThrowable = null
    }
}

const val VALIDATE_USE_PROMO_REVAMP_QUERY = "validate_use_promo_revamp"

const val VALIDATE_USE_PROMO_REVAMP_DEFAULT_RESPONSE = "promo/validate_use_promo_revamp_empty_response.json"

const val VALIDATE_USE_PROMO_REVAMP_BBO_APPLIED_RESPONSE = "promo/validate_use_promo_revamp_bbo_applied_response.json"