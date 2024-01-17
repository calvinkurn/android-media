package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import java.util.Date

sealed class AutomateCouponModel {
    data class List(
        val type: DynamicColorText,
        val benefit: DynamicColorText,
        val tnc: DynamicColorText,
        val backgroundUrl: String,
        val iconUrl: String?,
        val timeLimitPrefix: DynamicColorText?,
        val endDate: Date?,
        val shopName: DynamicColorText?,
        val badgeText: String?
    ) : AutomateCouponModel()

    data class Grid(
        val type: DynamicColorText,
        val benefit: DynamicColorText,
        val tnc: DynamicColorText,
        val backgroundUrl: String,
        val iconUrl: String?,
        val shopName: DynamicColorText?,
        val badgeText: String?
    ) : AutomateCouponModel()
}
