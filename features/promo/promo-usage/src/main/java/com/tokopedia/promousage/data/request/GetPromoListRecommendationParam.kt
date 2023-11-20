package com.tokopedia.promousage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

data class GetPromoListRecommendationParam(
    @SerializedName("params")
    val params: Map<String, Any>,
    @SerializedName("chosen_address")
    val chosenAddress: ChosenAddress? = null,
    @SerializedName("is_promo_revamp")
    val isPromoRevamp: Boolean = false
) : GqlParam {
    companion object {
        private const val KEY_PROMO = "promo"

        fun create(
            promoRequest: PromoRequest,
            chosenAddress: ChosenAddress?,
            isPromoRevamp: Boolean
        ): GetPromoListRecommendationParam {
            return GetPromoListRecommendationParam(
                params = mapOf(KEY_PROMO to promoRequest),
                chosenAddress = chosenAddress,
                isPromoRevamp = isPromoRevamp
            )
        }
    }
}
