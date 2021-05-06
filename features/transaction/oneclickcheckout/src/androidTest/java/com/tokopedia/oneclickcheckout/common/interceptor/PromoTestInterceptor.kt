package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PromoTestInterceptor : BaseOccInterceptor() {

    var customValidateUseResponsePath: String? = null
    var customValidateUseThrowable: IOException? = null

    var customClearPromoResponsePath: String? = null
    var customClearPromoThrowable: IOException? = null

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
        if (requestString.contains(CLEAR_CACHE_AUTO_APPLY_STACK_QUERY)) {
            if (customClearPromoThrowable != null) {
                throw customClearPromoThrowable!!
            } else if (customClearPromoResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customClearPromoResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(CLEAR_CACHE_AUTO_APPLY_STACK_DEFAULT_RESPONSE))
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

const val VALIDATE_USE_PROMO_REVAMP_BOE_APPLIED_RESPONSE = "promo/validate_use_promo_revamp_boe_applied_response.json"

const val VALIDATE_USE_PROMO_REVAMP_CASHBACK_FULL_APPLIED_RESPONSE = "promo/validate_use_promo_revamp_cashback_full_applied.json"

const val VALIDATE_USE_PROMO_REVAMP_CASHBACK_HALF_APPLIED_RESPONSE = "promo/validate_use_promo_revamp_cashback_half_applied.json"

const val VALIDATE_USE_PROMO_REVAMP_CASHBACK_RED_STATE_RESPONSE = "promo/validate_use_promo_revamp_cashback_red_state.json"

const val CLEAR_CACHE_AUTO_APPLY_STACK_QUERY = "clearCacheAutoApplyStack"

const val CLEAR_CACHE_AUTO_APPLY_STACK_DEFAULT_RESPONSE = "promo/clear_cache_auto_apply_stack_default_response.json"