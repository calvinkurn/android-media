package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class MvcShippingBenefit(
        @SerializedName("benefit_amount")
        val benefitAmount: Int = 0,
        @SerializedName("sp_id")
        val spId: Int = 0
)