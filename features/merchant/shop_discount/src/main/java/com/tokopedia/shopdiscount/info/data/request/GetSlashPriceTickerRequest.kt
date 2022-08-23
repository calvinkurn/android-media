package com.tokopedia.shopdiscount.info.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class GetSlashPriceTickerRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("platform")
    var platform: String = ""
)