package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-18.
 */

data class TrackingDetail(
        @field:SerializedName("product_id")
        val productId: Long = 0,

        @field:SerializedName("promo_codes_tracking")
        val promoCodesTracking: String = "",

        @field:SerializedName("promo_details_tracking")
        val promoDetailsTracking: String = ""
)