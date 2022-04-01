package com.tokopedia.shopdiscount.common.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestHeader(
    @SerializedName("source")
    @Expose
    var source: String = "",
    @SerializedName("ip")
    @Expose
    var ip: String = "",
    @SerializedName("usecase")
    @Expose
    var usecase: String = ""
)