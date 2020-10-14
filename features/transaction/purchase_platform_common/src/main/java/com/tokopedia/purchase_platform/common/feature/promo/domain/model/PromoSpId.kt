package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class PromoSpId(
        @SerializedName("unique_id")
        val uniqueId: String = "",
        @SerializedName("mvc_shipping_benefits")
        val mvcShippingBenefits: List<MvcShippingBenefit> = emptyList()
)