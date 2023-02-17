package com.tokopedia.logisticcart.boaffordability.model

import com.google.gson.annotations.SerializedName

class BoAffordabilityTexts(
    @SerializedName("ticker_cart")
    val tickerCart: String = "",
    @SerializedName("ticker_progressive")
    val tickerProgressive: String = ""
)
