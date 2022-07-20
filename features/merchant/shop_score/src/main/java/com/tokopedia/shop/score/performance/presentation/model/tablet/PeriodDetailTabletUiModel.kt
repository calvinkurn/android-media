package com.tokopedia.shop.score.performance.presentation.model.tablet

import com.tokopedia.shop.score.performance.presentation.adapter.tablet.DetailPerformanceAdapterTabletTypeFactory
import com.tokopedia.shop.score.performance.presentation.model.BasePeriodDetailUiModel

data class PeriodDetailTabletUiModel(
    override val period: String = "",
    override val nextUpdate: String = "",
    override val isNewSeller: Boolean = false
) : BaseParameterDetail, BasePeriodDetailUiModel(period, nextUpdate, isNewSeller) {

    override fun type(typeFactory: DetailPerformanceAdapterTabletTypeFactory): Int {
        return typeFactory.type(this)
    }
}