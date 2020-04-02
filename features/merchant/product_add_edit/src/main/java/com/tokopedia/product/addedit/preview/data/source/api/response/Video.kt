package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Video(
        @SerializedName("source")
        val source: String,
        @SerializedName("url")
        val url: String
)