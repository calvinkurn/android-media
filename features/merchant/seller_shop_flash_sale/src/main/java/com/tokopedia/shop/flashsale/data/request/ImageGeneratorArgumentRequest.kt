package com.tokopedia.shop.flashsale.data.request


import com.google.gson.annotations.SerializedName

data class ImageGeneratorArgumentRequest(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
)
