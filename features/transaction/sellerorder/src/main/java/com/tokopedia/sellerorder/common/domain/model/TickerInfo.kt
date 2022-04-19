package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerInfo(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("action_text")
        @Expose
        val actionText: String = "",

        @SerializedName("action_key")
        @Expose
        val actionKey: String = "",

        @SerializedName("action_url")
        @Expose
        val actionUrl: String = ""
)