package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerContainer(
    @Expose
    @SerializedName("id")
    var id: Int = 0,
    @Expose
    @SerializedName("type")
    var type: String = "",
    @Expose
    @SerializedName("metadata")
    var metadata: MutableList<TickerMetadata> = mutableListOf()
)
