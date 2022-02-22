package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class TradeInValidateImeiModel(
    @SerializedName("validateImei")
    var validateImei: ValidateImei
) {
    data class ValidateImei(
        @SerializedName("IsValid")
        var isValid: Boolean,
        @SerializedName("Message")
        var message: String
    )
}