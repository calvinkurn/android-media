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
        var listTicker: List<String> = listOf(),
        @SerializedName("tickerUnificationConfig")
        @Expose
        var tickerUnificationConfig: TickerUnificationConfig = TickerUnificationConfig()
    ){
        data class TickerUnificationConfig(
            @SerializedName("TickerUnificationTarget")
            @Expose
            var target: Target = Target()
        ){
            data class Target(
                @SerializedName("Type")
                @Expose
                var type: String = "",
                @SerializedName("Values")
                @Expose
                var listValue: List<String> = listOf()
            )
        }
    }
}
