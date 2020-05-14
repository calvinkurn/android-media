package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlAllDepositSummaryResponse(
        @SerializedName("allDepositHistory")
        var allDepositHistory: DepositActivityResponse? = null,

        @SerializedName("buyerDepositHistory")
        var buyerDepositHistory: DepositActivityResponse? = null,

        @SerializedName("sellerDepositHistory")
        var sellerDepositHistory: DepositActivityResponse? = null

)
