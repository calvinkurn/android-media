package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeVideo(
        @SerializedName("source")
        @Expose
        val source: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)