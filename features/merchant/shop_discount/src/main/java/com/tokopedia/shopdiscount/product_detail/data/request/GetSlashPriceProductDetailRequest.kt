package com.tokopedia.shopdiscount.product_detail.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.RequestHeader

data class GetSlashPriceProductDetailRequest(
    @SerializedName("request_header")
    @Expose
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("filter")
    @Expose
    var slashPriceProductDetailFilter: SlashPriceProductDetailFilter = SlashPriceProductDetailFilter(),
){
    data class SlashPriceProductDetailFilter(
        @SerializedName("product_ids")
        @Expose
        var listProductId: List<String> = listOf(),
        @SerializedName("status")
        @Expose
        var status: Int = -1,
    )
}