package com.tokopedia.checkout.data.model.response.egold

import com.google.gson.annotations.SerializedName

data class EgoldMessage(
    @SerializedName("title_text")
    val titleText: String = "",
    @SerializedName("sub_text")
    val subText: String = "",
    @SerializedName("ticker_text")
    val tickerText: String = "",
    @SerializedName("tooltip_text")
    val tooltipText: String = ""
)
