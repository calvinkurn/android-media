package com.tokopedia.buyerorder.detail.data


import com.google.gson.annotations.SerializedName

data class TickerInfo(
    @SerializedName("text")
    val text: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("action_text")
    val actionText: String = "",

    @SerializedName("action_key")
    val actionKey: String = "",

    @SerializedName("action_url")
    val actionUrl: String = ""
)