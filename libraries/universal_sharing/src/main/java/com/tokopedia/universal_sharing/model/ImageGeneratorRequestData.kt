package com.tokopedia.universal_sharing.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageGeneratorRequestData(
    @SerializedName("key")
    @Expose
    val key : String,
    @SerializedName("value")
    @Expose
    val value: String)