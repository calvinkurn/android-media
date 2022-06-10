package com.tokopedia.shopdiscount.common.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig

data class RequestHeader(
    @SerializedName("source")
    var source: String = "android",
    @SerializedName("ip")
    var ip: String = "",
    @SerializedName("usecase")
    var usecase: String = "",
    @SerializedName("version")
    var version: String = "android ${GlobalConfig.VERSION_NAME}"
)