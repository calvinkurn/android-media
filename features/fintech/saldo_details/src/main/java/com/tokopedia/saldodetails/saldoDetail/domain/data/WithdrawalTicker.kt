package com.tokopedia.saldodetails.saldoDetail.domain.data

import com.google.gson.annotations.SerializedName

data class WithdrawalTicker(
        @SerializedName("Ticker")
        var tickerMessage: String? = null
)
