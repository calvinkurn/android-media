package com.tokopedia.promousage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest

data class GetCouponListRecommendationParam(
    @SerializedName("params")
    val params: Map<Any, Any?>
) {
    companion object {
        private const val KEY_PROMO = "promo"

        fun create(
            promoRequest: PromoRequest,
            chosenAddress: ChosenAddress?
        ): GetCouponListRecommendationParam {
            return GetCouponListRecommendationParam(
                mapOf(
                    KEY_PROMO to promoRequest,
                    ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddress
                )
            )
        }
    }
}
