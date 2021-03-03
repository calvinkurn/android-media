package com.tokopedia.shop.score.performance.presentation.adapter

import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel

interface ShopPerformanceTypeFactory {
    fun type(headerShopPerformanceUiModel: HeaderShopPerformanceUiModel): Int
    fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int
}