package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class TrackingDetailsItem(
    @field:SerializedName("promo_details_tracking")
    val promoDetailsTracking: String = "",
    @field:SerializedName("product_id")
    val productId: Long = 0,
    @field:SerializedName("promo_codes_tracking")
    val promoCodesTracking: String = ""
)
