package com.tokopedia.saldodetails.feature_saldo_transaction_history.domain.data

import com.google.gson.annotations.SerializedName

data class GqlCompleteTransactionResponse(
        @SerializedName("allDepositHistory")
        var allDepositHistory: DepositActivityResponse? = null
)
