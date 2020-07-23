package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-09-29.
 */

data class FreeShipping(
        @SerializedName("eligible")
        val eligible: Boolean = false,

        @SerializedName("badge_url")
        val badgeUrl: String = ""
)