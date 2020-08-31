package com.tokopedia.saldodetails.response.model.saldoholdinfo.response

import com.google.gson.annotations.SerializedName

data class SaldoHoldResponse(

        @SerializedName("saldoHoldDepositHistory")
        var saldoHoldDepositHistory: SaldoHoldDepositHistory? = null
)
