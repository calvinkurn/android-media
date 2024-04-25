package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class RatesTickers(
    @SerializedName("top")
    val top: List<RatesTickerData> = listOf(),
    @SerializedName("bottom")
    val bottom: List<RatesTickerData> = listOf()
)
