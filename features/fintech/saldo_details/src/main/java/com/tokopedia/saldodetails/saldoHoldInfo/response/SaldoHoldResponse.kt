package com.tokopedia.saldodetails.saldoHoldInfo.response

import com.google.gson.annotations.SerializedName

data class SaldoHoldResponse(

        @SerializedName("saldoHoldDepositHistory")
        var saldoHoldDepositHistory: SaldoHoldDepositHistory? = null
)
