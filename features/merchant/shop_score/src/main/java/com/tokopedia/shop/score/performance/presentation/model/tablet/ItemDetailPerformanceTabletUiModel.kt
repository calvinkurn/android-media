package com.tokopedia.shop.score.performance.presentation.model.tablet

import com.tokopedia.shop.score.performance.presentation.adapter.tablet.DetailPerformanceAdapterTabletTypeFactory
import com.tokopedia.shop.score.performance.presentation.model.BaseDetailPerformanceUiModel

data class ItemDetailPerformanceTabletUiModel(
    override var titleDetailPerformance: String = "",
    override var valueDetailPerformance: String = "-",
    override var colorValueDetailPerformance: String = "",
    override var targetDetailPerformance: String = "",
    override var isDividerHide: Boolean = false,
    override var identifierDetailPerformance: String = "",
    override var parameterValueDetailPerformance: String = "",
    override var shopAge: Long = 0,
    override var shopScore: Long = -1
) : BaseDetailPerformanceUiModel(
    titleDetailPerformance,
    valueDetailPerformance,
    colorValueDetailPerformance,
    targetDetailPerformance,
    isDividerHide,
    identifierDetailPerformance,
    parameterValueDetailPerformance,
    shopAge,
    shopScore
), BaseParameterDetail {
    override fun type(typeFactory: DetailPerformanceAdapterTabletTypeFactory): Int {
        return typeFactory.type(this)
    }
}