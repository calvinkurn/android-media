package com.tokopedia.saldodetails.feature_saldo_detail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlWithdrawalTickerResponse (
        @SerializedName("withdrawalTicker")
        var withdrawalTicker: WithdrawalTicker? = null
)
