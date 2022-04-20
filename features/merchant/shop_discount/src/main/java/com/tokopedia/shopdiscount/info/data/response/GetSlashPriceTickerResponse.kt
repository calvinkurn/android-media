package com.tokopedia.shopdiscount.info.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class GetSlashPriceTickerResponse(
    @SerializedName("GetSlashPriceTicker")
    @Expose
    var getSlashPriceTicker: GetSlashPriceTicker = GetSlashPriceTicker()
) {
    data class GetSlashPriceTicker(
        @SerializedName("response_header")
        @Expose
        var responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("tickers")
        @Expose
        var listTicker: List<String> = listOf()
    )
}