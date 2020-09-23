package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class TnCInfoModel(
    @SerializedName("fetchTickerAndTnC")
    val fetchTickerAndTnC: FetchTickerAndTnC
) {
    data class FetchTickerAndTnC(
        @SerializedName("TnC")
        val tnC: List<String>,
        var type : Int = 0
    )
}