package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class HomeTickerData(
        @SerializedName("ticker", alternate = ["getHomeTickerV2"])
        var ticker: Ticker = Ticker()
)
