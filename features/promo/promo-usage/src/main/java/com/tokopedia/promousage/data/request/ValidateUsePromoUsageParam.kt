package com.tokopedia.promousage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest

data class ValidateUsePromoUsageParam(
    @SerializedName("params")
    val params: Map<Any, Any?>,
    @SerializedName("chosen_address")
    val chosenAddress: ChosenAddress
) : GqlParam {
    companion object {
        private const val KEY_PROMO = "promo"

        fun create(
            validateUsePromoRequest: ValidateUsePromoRequest,
            chosenAddress: ChosenAddress
        ): ValidateUsePromoUsageParam {
            return ValidateUsePromoUsageParam(
                mapOf(KEY_PROMO to validateUsePromoRequest),
                chosenAddress
            )
        }
    }
}
