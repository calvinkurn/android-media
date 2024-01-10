package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import java.util.Date

data class AutomateCouponModel(
    val type: String,
    val benefit: String,
    val freeText: String,
    val backgroundUrl: String,
    val iconUrl: String,
    val endDate: Date?,
    val badgeText: String?
)
