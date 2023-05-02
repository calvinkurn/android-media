package com.tokopedia.topads.common.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopAdsTickerV2(
    @SerializedName("topAdsTickerV2")
    @Expose
    var topAdsTicker: TopAdsTickerResponse
)
