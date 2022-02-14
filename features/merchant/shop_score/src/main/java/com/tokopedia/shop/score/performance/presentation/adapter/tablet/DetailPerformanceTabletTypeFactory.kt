package com.tokopedia.shop.score.performance.presentation.adapter.tablet

import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemDetailPerformanceTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.PeriodDetailTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.ProtectedParameterTabletUiModel

interface DetailPerformanceTabletTypeFactory {
    fun type(periodDetailTabletUiModel: PeriodDetailTabletUiModel): Int
    fun type(itemDetailPerformanceTabletUiModel: ItemDetailPerformanceTabletUiModel): Int
    fun type(protectedParameterTabletUiModel: ProtectedParameterTabletUiModel): Int
}