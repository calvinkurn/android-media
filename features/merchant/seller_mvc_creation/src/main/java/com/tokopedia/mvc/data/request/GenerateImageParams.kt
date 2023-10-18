package com.tokopedia.mvc.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenerateImageParams(
    @SerializedName("key")
    @Expose
    val key: String,
    @SerializedName("value")
    @Expose
    val value: String
)
