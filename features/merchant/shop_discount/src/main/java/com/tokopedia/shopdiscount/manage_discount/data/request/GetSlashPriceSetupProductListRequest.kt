package com.tokopedia.shopdiscount.manage_discount.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class GetSlashPriceSetupProductListRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("request_id")
    var requestId: String = ""
)