package com.tokopedia.shop.score.performance.presentation.adapter.tablet

import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel

interface DetailPerformanceTabletTypeFactory {
    fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int
    fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int
    fun type(protectedParameterSectionUiModel: ProtectedParameterSectionUiModel): Int
}