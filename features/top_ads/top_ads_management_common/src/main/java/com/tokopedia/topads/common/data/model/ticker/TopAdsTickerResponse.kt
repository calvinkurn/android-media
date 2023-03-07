package com.tokopedia.topads.common.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopAdsTickerResponse (
    @SerializedName("data")
    @Expose
    var data: DataMessage,

    @SerializedName("status")
    @Expose
    var status: Status

)
