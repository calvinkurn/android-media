package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerInfo(
        @SerializedName("label")
        @Expose
        val message: String = "",

        @SerializedName("color")
        @Expose
        val textColor: String = "",

        @SerializedName("bg_color")
        @Expose
        val backgroundColor: String = "",

        @SerializedName("link_text")
        @Expose
        val linkText: String = "",

        @SerializedName("link_url")
        @Expose
        val linkUrl: String = ""
)