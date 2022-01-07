package com.tokopedia.logisticorder.domain.response

import com.google.gson.annotations.SerializedName

data class GetDriverTipResponse(
    @SerializedName("mpLogisticDriverTipInfo")
    val response: LogisticDriverTipResponse = LogisticDriverTipResponse()
)

data class LogisticDriverTipResponse(
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("last_driver")
    val lastDriver: LastDriver = LastDriver(),
    val statusSubtitle: String = "",
    @SerializedName("prepayment")
    val prepayment: Prepayment = Prepayment(),
    @SerializedName("payment")
    val payment: Payment = Payment()
)

data class Prepayment(
    @SerializedName("info")
    val info: List<String> = listOf(),
    @SerializedName("preset_amount")
    val presetAmount: List<Int> = listOf(),
    @SerializedName("max_amount")
    val maxAmount: Int =  0,
    @SerializedName("min_amount")
    val minAmount: Int =  0,
    @SerializedName("payment_link")
    val paymentLink: String = ""
)

data class Payment(
    @SerializedName("amount")
    val amount: Int = 0,
    @SerializedName("amount_formatted")
    val amountFormatted: String = "",
    @SerializedName("method")
    val method: String = "",
    @SerializedName("method_icon")
    val methodIcon: String = ""
)