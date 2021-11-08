package com.tokopedia.saldodetails.saldoDetail.domain.data

import com.google.gson.annotations.SerializedName

data class GqlWithdrawalTickerResponse (
        @SerializedName("withdrawalTicker")
        var withdrawalTicker: WithdrawalTicker? = null
)
