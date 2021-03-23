package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LabelGroup(
        @SerializedName("position")
        @Expose
        val position: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)