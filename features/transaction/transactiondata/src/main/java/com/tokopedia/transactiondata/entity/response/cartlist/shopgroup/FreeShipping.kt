package com.tokopedia.transactiondata.entity.response.cartlist.shopgroup

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-09-29.
 */

data class FreeShipping(
        @SerializedName("eligible")
        val eligible: Boolean,

        @SerializedName("badge_url")
        val badgeUrl: String
)