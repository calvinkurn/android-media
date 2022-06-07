package com.tokopedia.shopdiscount.common.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig

data class RequestHeader(
    @SerializedName("source")
    @Expose
    var source: String = "android",
    @SerializedName("ip")
    @Expose
    var ip: String = "",
    @SerializedName("usecase")
    @Expose
    var usecase: String = "",
    @SerializedName("version")
    @Expose
    var version: String = "android ${GlobalConfig.VERSION_NAME}"
)