package com.tokopedia.shopdiscount.select.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING

data class GetSlashPriceProductListToReserveRequest(
    @SerializedName("request_header")
    @Expose
    val requestHeader: RequestHeader,

    @SerializedName("request_id")
    @Expose
    val requestId: String,

    @SerializedName("filter")
    @Expose
    val filter: FilterRequest,

    @SerializedName("sort")
    @Expose
    val sort: SortRequest
)