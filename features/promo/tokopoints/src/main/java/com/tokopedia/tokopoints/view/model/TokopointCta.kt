package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointCta(
    @Expose
    @SerializedName("icon")
    var icon: String = "",
    @Expose
    @SerializedName("text")
    var text: String = "",
    @Expose
    @SerializedName("url")
    var url: String = "",
    @Expose
    @SerializedName("applink")
    var applink: String = "",
    @Expose
    @SerializedName("type")
    var type: String = "",
)
