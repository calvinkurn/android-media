package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopoints.view.model.TickerContainer

data class LayoutTickerAttr(
    @SerializedName("tickerList")
    @Expose
    var tickerList: List<TickerContainer> = listOf()
)
