package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

data class OccTickerMessage(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("replacement")
        val replacement: List<OccTickerMessageReplacement> = emptyList()
)

data class OccTickerMessageReplacement(
        @SerializedName("identifier")
        val identifier: String = "",
        @SerializedName("value")
        val value: String = ""
)