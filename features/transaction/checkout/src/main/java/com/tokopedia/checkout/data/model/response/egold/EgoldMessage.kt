package com.tokopedia.checkout.data.model.response.egold

import com.google.gson.annotations.SerializedName

data class EgoldMessage(
    @SerializedName("title_text")
    var titleText: String? = null,
    @SerializedName("sub_text")
    var subText: String? = null,
    @SerializedName("ticker_text")
    var tickerText: String? = null,
    @SerializedName("tooltip_text")
    var tooltipText: String? = null
)