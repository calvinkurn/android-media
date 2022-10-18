package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TickerMetadata(
    @Expose
    @SerializedName("text")
    var text: Map<String, String> = mapOf(),
    @Expose
    @SerializedName("image")
    var image: Map<String, String> = mapOf(),
    @Expose
    @SerializedName("link")
    var link: Map<String, String> = mapOf(),
)
