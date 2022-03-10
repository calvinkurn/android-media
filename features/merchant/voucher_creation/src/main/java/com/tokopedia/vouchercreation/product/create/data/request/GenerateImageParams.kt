package com.tokopedia.vouchercreation.product.create.data.request

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