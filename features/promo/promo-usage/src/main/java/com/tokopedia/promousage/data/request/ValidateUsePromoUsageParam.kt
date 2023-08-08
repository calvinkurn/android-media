package com.tokopedia.promousage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

data class ValidateUsePromoUsageParam(
    @SerializedName("params")
    val params: Map<Any, Any?>
) {
    companion object {
        private const val KEY_PROMO = "promo"

        fun create(validateUsePromoRequest: ValidateUsePromoRequest): ValidateUsePromoUsageParam {
            return ValidateUsePromoUsageParam(
                mapOf(KEY_PROMO to validateUsePromoRequest)
            )
        }
    }
}
