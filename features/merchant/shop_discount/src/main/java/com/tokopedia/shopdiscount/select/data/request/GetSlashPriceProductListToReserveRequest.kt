package com.tokopedia.shopdiscount.select.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class GetSlashPriceProductListToReserveRequest(
    @SerializedName("request_header")
    val requestHeader: RequestHeader,

    @SerializedName("request_id")
    val requestId: String,

    @SerializedName("filter")
    val filter: FilterRequest,

    @SerializedName("sort")
    val sort: SortRequest
)