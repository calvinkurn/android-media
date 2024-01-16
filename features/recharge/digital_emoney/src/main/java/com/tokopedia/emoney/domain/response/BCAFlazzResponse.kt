package com.tokopedia.emoney.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.emoney.domain.request.CommonBodyEnc

class BCAFlazzResponse (
    @SerializedName("rechargeUpdateBalanceEmoneyBcaFlazz")
    val data: CommonBodyEnc = CommonBodyEnc()
)

data class BCAFlazzData(
    @SerializedName("Action")
    val action: Int = 0,
    @SerializedName("Status")
    val status: Int = 0,
    @SerializedName("Attributes")
    val attributes: BCAFlazzAttribute = BCAFlazzAttribute(),
)

data class BCAFlazzAttribute(
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
    @SerializedName("ButtonText")
    val buttonText: String = "",
    @SerializedName("ImageIssuer")
    val imageIssuer: String = "",
    @SerializedName("Message")
    val message: String = "",
    @SerializedName("HasMorePendingBalance")
    val hasMorePendingBalance: Boolean = false,
    @SerializedName("AccessCardNumber")
    val accessCardNumber: String = "",
    @SerializedName("AccessCode")
    val accessCode: String = "",
)
