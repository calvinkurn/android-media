package com.tokopedia.cartrevamp.data.model.response.promo

import com.google.gson.annotations.SerializedName

data class TrackingDetail(
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("promo_codes_tracking")
    val promoCodesTracking: String = "",
    @SerializedName("promo_details_tracking")
    val promoDetailsTracking: String = ""
)
