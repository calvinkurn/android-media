package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class BenefitDetailsItem(
        @field:SerializedName("code")
        val code: String = "",
        @field:SerializedName("unique_id")
        val uniqueId: String = "",
        @field:SerializedName("cashback_amount")
        val cashbackAmount: Int = 0,
        @field:SerializedName("promo_type")
        val promoType: PromoType = PromoType(),
        @field:SerializedName("discount_amount")
        val discountAmount: Int = 0,
        @field:SerializedName("benefit_product_details")
        val benefitProductDetails: List<BenefitProductDetailsItem> = emptyList(),
        @field:SerializedName("type")
        val type: String = ""
)