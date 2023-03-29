package com.tokopedia.emoney.domain.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class JakCardRequest(
    @SerializedName("body")
    val body: JakCardBody = JakCardBody(),
): GqlParam

data class JakCardBody(
    @SerializedName("cardNumber")
    val cardNumber: String = "",
    @SerializedName("cardData")
    val cardData: String = "",
    @SerializedName("amount")
    val amount: Int = 0,
    @SerializedName("lastBalance")
    val lastBalance: Int = 0,
    @SerializedName("stan")
    val stan: String = "",
    @SerializedName("refNo")
    val refNo: String = "",
    @SerializedName("action")
    val action: Int = 0,
)

enum class JakCardAction(val action: Int) {
    GET_PENDING_BALANCE(1),
    TOP_UP(2),
    TOP_UP_CONFIRMATION(3),
}

enum class JakCardStatus(val status: Int) {
    WRITE(0),
    DONE(1),
    ERROR(2)
}
