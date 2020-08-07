package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerInfo(
        @SerializedName("text")
        @Expose
        val message: String = "",

        @SerializedName("color")
        @Expose
        val textColor: String = "",

        @SerializedName("type")
        @Expose
        val backgroundColor: String = "",

        @SerializedName("action_text")
        @Expose
        val linkText: String = "",

        @SerializedName("action_url")
        @Expose
        val linkUrl: String = ""
)