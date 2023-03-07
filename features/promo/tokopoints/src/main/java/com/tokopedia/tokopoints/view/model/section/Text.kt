package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Text(
    @SerializedName("content")
    @Expose
    var content: String = "",
    @SerializedName("color")
    @Expose
    var color: String = "",
    @SerializedName("format")
    @Expose
    var format: String = "",
)
