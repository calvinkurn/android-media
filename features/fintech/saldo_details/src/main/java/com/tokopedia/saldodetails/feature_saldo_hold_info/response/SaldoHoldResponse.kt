package com.tokopedia.saldodetails.feature_saldo_hold_info.response

import com.google.gson.annotations.SerializedName

data class SaldoHoldResponse(

        @SerializedName("saldoHoldDepositHistory")
        var saldoHoldDepositHistory: SaldoHoldDepositHistory? = null
)
