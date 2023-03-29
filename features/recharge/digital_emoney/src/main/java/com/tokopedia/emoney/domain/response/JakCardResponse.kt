package com.tokopedia.emoney.domain.response

import com.google.gson.annotations.SerializedName

data class JakCardResponse (
    @SerializedName("rechargeUpdateBalanceEmoneyDkiJakcard")
    val data: JakCardData = JakCardData(),
)

data class JakCardData(
    @SerializedName("action")
    val action: Int = 0,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("attributes")
    val attributes: JakCardAttribute = JakCardAttribute(),
)

data class JakCardAttribute(
    @SerializedName("card_number")
    val cardNumber: String = "",
    @SerializedName("cryptogram")
    val cryptogram: String = "",
    @SerializedName("last_balance")
    val lastBalance: Int = 0,
    @SerializedName("button_text")
    val buttonText: String = "",
    @SerializedName("image_issuer")
    val imageIssuer: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("stan")
    val stan: String = "",
    @SerializedName("ref_no")
    val refNo: String = "",
)
