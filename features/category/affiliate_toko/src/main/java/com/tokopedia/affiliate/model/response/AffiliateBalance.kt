package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateBalance(
    @SerializedName("getAffiliateBalance")
    var affiliateBalance: AffiliateBalance?
) {
    data class AffiliateBalance(
        @SerializedName("Data")
        var balanceData: Data?
    ) {
        data class Data(
            @SerializedName("Amount")
            var amount: String?,
            @SerializedName("AmountFormatted")
            var amountFormatted: String?,
            @SerializedName("Error")
            var error: Error?,
            @SerializedName("Status")
            var status: Int?,
            @SerializedName("WalletStatus")
            var walletStatus: String?
        ) {
            data class Error(
                @SerializedName("ErrorType")
                var errorType: Int?,
                @SerializedName("Message")
                var message: String?
            )
        }
    }
}
