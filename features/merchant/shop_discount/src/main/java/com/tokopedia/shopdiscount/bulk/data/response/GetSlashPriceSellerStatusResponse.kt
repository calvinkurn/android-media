package com.tokopedia.shopdiscount.bulk.data.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class GetSlashPriceSellerStatusResponse(
    @SerializedName("getSlashPriceSellerStatus")
    val getSlashPriceSellerStatus: GetSlashPriceSellerStatus = GetSlashPriceSellerStatus()
) {
    data class GetSlashPriceSellerStatus(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("list_key_targetted_ticker")
        val listKey: List<String> = listOf(),
    )
}
