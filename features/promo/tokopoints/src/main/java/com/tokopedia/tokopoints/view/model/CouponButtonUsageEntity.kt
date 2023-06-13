package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CouponButtonUsageEntity(
    @Expose
    @SerializedName(value = "appLink", alternate = ["applink"])
    var appLink: String = "",
    @Expose
    @SerializedName("text")
    var text: String = "",
    @Expose
    @SerializedName("type")
    var type: String = "",
    @Expose
    @SerializedName("url")
    var url: String = "",
)
