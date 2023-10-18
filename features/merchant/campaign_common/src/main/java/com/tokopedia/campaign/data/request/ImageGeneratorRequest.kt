package com.tokopedia.campaign.data.request

import com.google.gson.annotations.SerializedName

data class ImageGeneratorRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)
