package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-18.
 */

data class TrackingDetails(
        @field:SerializedName("product_id")
        val productId: Int = 0,

        @field:SerializedName("promo_codes_tracking")
        val promoCodesTracking: String = "",

        @field:SerializedName("promo_details_tracking")
        val promoDetailsTracking: String = ""
)