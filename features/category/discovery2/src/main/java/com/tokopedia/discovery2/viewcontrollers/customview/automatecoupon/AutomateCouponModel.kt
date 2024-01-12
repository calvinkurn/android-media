package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import java.util.Date

data class AutomateCouponModel(
    val type: DynamicColorText,
    val benefit: DynamicColorText,
    val tnc: DynamicColorText,
    val backgroundUrl: String,
    val iconUrl: String?,
    val timeLimitPrefix: DynamicColorText?,
    val endDate: Date?,
    val shopName: DynamicColorText?,
    val badgeText: String?
)
