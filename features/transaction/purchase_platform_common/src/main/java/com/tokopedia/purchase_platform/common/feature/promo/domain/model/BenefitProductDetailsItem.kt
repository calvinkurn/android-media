package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class BenefitProductDetailsItem(
    @field:SerializedName("cashback_amount")
    val cashbackAmount: Int = 0,
    @field:SerializedName("discount_amount")
    val discountAmount: Int = 0,
    @field:SerializedName("is_bebas_ongkir")
    val isBebasOngkir: Boolean = false
)
