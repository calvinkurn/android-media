package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class MembershipDetailUiModel(
    val periodDate: String = String.EMPTY,
    val gradeName: String = String.EMPTY,
    val nextUpdate: String = String.EMPTY,
    val isCalibrationDate: Boolean = false,
    val totalOrderLast90Days: Long = Int.ZERO.toLong(),
    val netIncomeLast90Days: Long = Int.ZERO.toLong(),
    val totalOrderLast30Days: Long = Int.ZERO.toLong(),
    val netIncomeLast30Days: Long = Int.ZERO.toLong(),
    val shopScore: Int = Int.ZERO
)