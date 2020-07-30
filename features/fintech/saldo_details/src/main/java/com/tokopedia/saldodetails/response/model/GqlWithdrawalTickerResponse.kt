package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlWithdrawalTickerResponse (
        @SerializedName("withdrawalTicker")
        var withdrawalTicker: WithdrawalTicker? = null
)
