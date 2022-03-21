package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Video(
        @SerializedName("source")
        @Expose
        val source: String,
        @SerializedName("url")
        @Expose
        val url: String
)