package com.tokopedia.discovery_component.widgets.automatecoupon

sealed class AutomateCouponModel {
    data class List(
        val type: DynamicColorText,
        val benefit: DynamicColorText,
        val tnc: DynamicColorText,
        val backgroundUrl: String,
        val timeLimit: TimeLimit,
        val iconUrl: String,
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
