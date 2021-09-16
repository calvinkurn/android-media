package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class OccTickerMessage(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("replacement")
        val replacement: List<OccTickerMessageReplacement> = emptyList()
)

class OccTickerMessageReplacement(
        @SerializedName("identifier")
        val identifier: String = "",
        @SerializedName("value")
        val value: String = ""
)