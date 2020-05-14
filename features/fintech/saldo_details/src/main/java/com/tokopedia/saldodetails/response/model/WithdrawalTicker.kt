package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class WithdrawalTicker(
        @SerializedName("Ticker")
        var tickerMessage: String? = null
)
