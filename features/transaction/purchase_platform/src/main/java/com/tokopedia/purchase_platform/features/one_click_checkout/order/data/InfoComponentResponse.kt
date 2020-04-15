package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class InfoComponentResponse(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = ""
)