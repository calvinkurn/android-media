package com.tokopedia.product.detail.data.model


import com.google.gson.annotations.SerializedName

data class TradeinResponse(
        @SerializedName("validateTradeInPDP")
        val validateTradeInPDP: ValidateTradeInPDP = ValidateTradeInPDP()
)

data class ValidateTradeInPDP(
        @SerializedName("IsDiagnosed")
        val isDiagnosed: Boolean = false,
        @SerializedName("IsEligible")
        val isEligible: Boolean = false,
        @SerializedName("Message")
        val message: String = "",
        @SerializedName("RemainingPrice")
        val remainingPrice: Int = 0,
        @SerializedName("UseKyc")
        val useKyc: Boolean = false,
        @SerializedName("UsedPrice")
        val usedPrice: Int = 0
)