package com.tokopedia.topads.dashboard.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseTickerTopads(
    @SerializedName("data")
    @Expose
    val data: Data
)
