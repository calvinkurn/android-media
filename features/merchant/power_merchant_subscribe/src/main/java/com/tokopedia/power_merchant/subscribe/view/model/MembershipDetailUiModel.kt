package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class MembershipDetailUiModel(
    val periodDate: String = String.EMPTY,
    val gradeName: String = String.EMPTY,
    val nextUpdate: String = String.EMPTY,
    val totalOrder: Long = Int.ZERO.toLong(),
    val netIncome: Long = Int.ZERO.toLong(),
    val shopScore: Int = Int.ZERO
)