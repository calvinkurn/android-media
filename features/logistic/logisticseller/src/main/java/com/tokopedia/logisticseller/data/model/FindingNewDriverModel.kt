package com.tokopedia.logisticseller.data.model

import java.util.*

data class FindingNewDriverModel(
    val invoice: String,
    val message: String,
    val availableTime: String,
    var isEnable: Boolean = false,
    var calendar: Calendar? = null,
)
