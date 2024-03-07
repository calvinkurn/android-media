package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LabelGroupStyle(
    @SerializedName("key")
    @Expose
    val key: String = "",

    @SerializedName("value")
    @Expose
    val value: String = ""
)
