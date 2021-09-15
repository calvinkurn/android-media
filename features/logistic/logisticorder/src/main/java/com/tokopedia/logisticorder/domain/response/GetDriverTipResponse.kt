package com.tokopedia.logisticorder.domain.response

import com.google.gson.annotations.SerializedName

data class GetDriverTipResponse(
    @SerializedName("logistic_driver_tip_info")
    val response: LogisticDriverTipResponse = LogisticDriverTipResponse()
)

data class LogisticDriverTipResponse(
    @SerializedName("message_error")
    val messageError: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("status_title")
    val statusTitle: String = "",
    @SerializedName("status_subtitle")
    val statusSubtitle: String = "",
    @SerializedName("data")
    val driverTipData: DriverTipData = DriverTipData()
)

data class DriverTipData(
    @SerializedName("last_driver")
    val lastDriver: LastDriver = LastDriver(),
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("prepayment")
    val prepayment: Prepayment = Prepayment(),
    @SerializedName("preset_amount")
    val presetAmount: List<Int> = listOf(),
    @SerializedName("max_amount")
    val maxAmount: Int =  0,
    @SerializedName("min_amount")
    val minAmount: Int =  0,
    @SerializedName("payment_link")
    val paymentLink: String = ""
)

data class Prepayment(
    @SerializedName("info")
    val info: List<String> = listOf()
)