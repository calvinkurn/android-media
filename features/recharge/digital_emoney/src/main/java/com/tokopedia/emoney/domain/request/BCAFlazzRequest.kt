package com.tokopedia.emoney.domain.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam


data class BCAFlazzRequest(
    @SerializedName("body")
    val body: CommonBodyEnc = CommonBodyEnc(),
): GqlParam

data class BCAFlazzBody(
    @SerializedName("CardNumber")
    val cardNumber: String = "",
    @SerializedName("CardData")
    val cardData: String = "",
    @SerializedName("Amount")
    val amount: Int = 0,
    @SerializedName("LastBalance")
    val lastBalance: Int = 0,
    @SerializedName("TransactionID")
    val transactionID: String = "",
    @SerializedName("CardType")
    val cardType: String = "",
    @SerializedName("Action")
    val action: Int = 0,
)

enum class BCAFlazzAction(val action: Int) {
    GET_PENDING_BALANCE(0),
    GENERATE_TRX_ID(1),
    GENERATE_SESSION_KEY(2),
    BETWEEN_TOP_UP(3),
    ACK_AFTER_TOP_UP_2(4),
    REVERSAL(5)
}

enum class BCAFlazzStatus(val status: Int) {
    WRITE(0),
    DONE(1),
    ERROR(2),
    REVERSAL(3)
}
