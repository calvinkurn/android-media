package com.tokopedia.logisticorder.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticorder.domain.response.LastDriver

data class DriverTipModel(
    var response: LogisticDriverModel = LogisticDriverModel()
)

data class LogisticDriverModel(
    var messageError: String = "",
    var status: Int = 0,
    var lastDriver: LastDriverModel = LastDriverModel(),
    var prepayment: PrepaymentModel = PrepaymentModel(),
    var payment: PaymentModel = PaymentModel()
)

data class PrepaymentModel(
    var info: List<String> = listOf(),
    var presetAmount: List<Int> = listOf(),
    var maxAmount: Int =  0,
    var minAmount: Int =  0,
    var paymentLink: String = ""
)

data class PaymentModel(
    var amount: Int = 0,
    var amountFormatted: String = "",
    var method: String = "",
    var methodIcon: String = ""
)