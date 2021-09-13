package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data

import com.google.gson.annotations.SerializedName

data class GqlCompleteTransactionResponse(
        @SerializedName("allDepositHistory")
        var allDepositHistory: DepositActivityResponse? = null
)
