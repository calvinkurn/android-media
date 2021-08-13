package com.tokopedia.saldodetails.feature_saldo_detail.domain.data

import com.google.gson.annotations.SerializedName

data class WithdrawalTicker(
        @SerializedName("Ticker")
        var tickerMessage: String? = null
)
