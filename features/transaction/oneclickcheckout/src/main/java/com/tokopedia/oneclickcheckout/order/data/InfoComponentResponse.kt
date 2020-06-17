package com.tokopedia.oneclickcheckout.order.data

import com.google.gson.annotations.SerializedName

data class InfoComponentResponse(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = ""
)