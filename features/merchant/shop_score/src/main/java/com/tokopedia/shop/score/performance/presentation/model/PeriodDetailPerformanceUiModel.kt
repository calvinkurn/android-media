package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class PeriodDetailPerformanceUiModel(
    override val period: String = "",
    override val nextUpdate: String = "",
    override val isNewSeller: Boolean = false
) : BaseShopPerformance, BasePeriodDetailUiModel(period, nextUpdate, isNewSeller) {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}