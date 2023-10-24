package com.tokopedia.cartcommon.data.response.common

import com.google.gson.annotations.SerializedName

data class ProductTagInfo(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = ""
)
