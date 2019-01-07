package com.tokopedia.topads.dashboard.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("topAdsTicker")
    @Expose
    var topAdsTicker: TopAdsTicker
)
