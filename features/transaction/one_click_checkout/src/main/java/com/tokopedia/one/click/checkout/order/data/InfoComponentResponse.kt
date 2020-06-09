package com.tokopedia.one.click.checkout.order.data

import com.google.gson.annotations.SerializedName

data class InfoComponentResponse(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = ""
)