package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig

data class RequestHeader(
    @SerializedName("source")
    val source: String = "android",
    @SerializedName("ip")
    val ip: String = "",
    @SerializedName("usecase")
    val usecase: String = "",
    @SerializedName("version")
    val version: String = GlobalConfig.VERSION_NAME
)