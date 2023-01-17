package com.tokopedia.affiliate.model.response
import com.google.gson.annotations.SerializedName

data class AffiliateDateFilterResponse(
    @SerializedName("getAffiliateDateFilter")
    var `data`: Data?
) {
    data class Data(
        @SerializedName("GetAffiliateDateFilter")
        var getAffiliateDateFilter: List<GetAffiliateDateFilter?>?,
        @SerializedName("Ticker")
        var ticker: List<Ticker?>?
    ) {
        data class GetAffiliateDateFilter(
            @SerializedName("UpdateDescription")
            var updateDescription: String?,
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
