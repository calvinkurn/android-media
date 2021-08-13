package com.tokopedia.saldodetails.feature_saldo_transaction_history.domain.data

import com.google.gson.annotations.SerializedName

data class GqlAllDepositSummaryResponse(
    @SerializedName("allDepositHistory")
        var allDepositHistory: DepositActivityResponse? = null,

    @SerializedName("buyerDepositHistory")
        var buyerDepositHistory: DepositActivityResponse? = null,

    @SerializedName("sellerDepositHistory")
        var sellerDepositHistory: DepositActivityResponse? = null

) {
    fun isHavingError(): Boolean {
        return (allDepositHistory?.isHaveError == true ||
                buyerDepositHistory?.isHaveError == true ||
                sellerDepositHistory?.isHaveError == true)

    }

    fun getErrorMessage(): String {
        return when {
            allDepositHistory?.isHaveError == true -> allDepositHistory?.message ?: ""
            buyerDepositHistory?.isHaveError == true -> allDepositHistory?.message ?: ""
            sellerDepositHistory?.isHaveError == true -> allDepositHistory?.message ?: ""
            else -> ""
        }
    }
}
