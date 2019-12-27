package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlCompleteTransactionResponse(
        @SerializedName("allDepositHistory")
        var allDepositHistory: DepositActivityResponse? = null
)
