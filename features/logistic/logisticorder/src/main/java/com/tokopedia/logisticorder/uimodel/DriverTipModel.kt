package com.tokopedia.logisticorder.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticorder.domain.response.LastDriver

data class DriverTipModel(
    var response: LogisticDriverModel = LogisticDriverModel()
)

data class LogisticDriverModel(
    var messageError: String = "",
    var status: Int = 0,
    var statusTitle: String = "",
    var statusSubtitle: String = "",
    var driverTipData: DriverTipDataModel = DriverTipDataModel()
)

data class DriverTipDataModel(
    var lastDriver: LastDriverModel = LastDriverModel(),
    var status: Int = 0,
    var prepayment: PrepaymentModel = PrepaymentModel(),
    var presetAmount: List<Int> = listOf(),
    var maxAmount: Int =  0,
    var minAmount: Int =  0,
    var paymentLink: String = ""
)

data class PrepaymentModel(
    var info: List<String> = listOf()
)