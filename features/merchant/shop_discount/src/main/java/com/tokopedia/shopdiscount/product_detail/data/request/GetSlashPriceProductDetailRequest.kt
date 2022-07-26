package com.tokopedia.shopdiscount.product_detail.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class GetSlashPriceProductDetailRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("filter")
    var slashPriceProductDetailFilter: SlashPriceProductDetailFilter = SlashPriceProductDetailFilter(),
){
    data class SlashPriceProductDetailFilter(
        @SerializedName("product_ids")
        var listProductId: List<String> = listOf(),
        @SerializedName("status")
        var status: Int = -1,
    )
}