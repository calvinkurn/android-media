package com.tokopedia.discovery_component.widgets.automatecoupon

sealed class AutomateCouponModel {
    data class List(
        val type: DynamicColorText? = null,
        val benefit: DynamicColorText? = null,
        val tnc: DynamicColorText? = null,
        val backgroundUrl: String,
        val timeLimit: TimeLimit? = null,
        val iconUrl: String,
        val shopName: DynamicColorText? = null,
        val badgeText: String? = null
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
