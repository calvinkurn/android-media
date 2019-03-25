package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

data class ClashingVoucherOrder(
        @SerializedName("code")
        val code: String = "",

        @SerializedName("unique_id")
        val uniqueId: String = "",

        @SerializedName("cart_id")
        val cartId: Int = 0,

        @SerializedName("promo_name")
        val promoName: String = "",

        @SerializedName("potential_benefit")
        val potentialBenefit: Int = 0
)