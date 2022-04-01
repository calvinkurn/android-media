package com.tokopedia.shop.score.performance.presentation.model

open class BaseDetailPerformanceUiModel(
    open var titleDetailPerformance: String = "",
    open var valueDetailPerformance: String = "-",
    open var colorValueDetailPerformance: String = "",
    open var targetDetailPerformance: String = "",
    open var isDividerHide: Boolean = false,
    open var identifierDetailPerformance: String = "",
    open var parameterValueDetailPerformance: String = "",
    open var shopAge: Long = 0,
    open var shopScore: Long = -1
)