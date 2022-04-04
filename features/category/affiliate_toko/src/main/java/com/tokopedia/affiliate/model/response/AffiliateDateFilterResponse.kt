package com.tokopedia.affiliate.model.response


import com.google.gson.annotations.SerializedName

data class AffiliateDateFilterResponse(
    @SerializedName("data")
    var `data`: Data?
) {
    data class Data(
        @SerializedName("getAffiliateDateFilter")
        var getAffiliateDateFilter: List<GetAffiliateDateFilter?>?,
        @SerializedName("ticker")
        var ticker: List<Ticker?>?
    ) {
        data class GetAffiliateDateFilter(
            @SerializedName("FilterDescription")
            var filterDescription: String?,
            @SerializedName("FilterTitle")
            var filterTitle: String?,
            @SerializedName("FilterType")
            var filterType: String?,
            @SerializedName("FilterValue")
            var filterValue: String?
        )
        data class Ticker(
            @SerializedName("TickerDescription")
            var tickerDescription: String?,
            @SerializedName("TickerType")
            var tickerType: String?
        )
    }
}