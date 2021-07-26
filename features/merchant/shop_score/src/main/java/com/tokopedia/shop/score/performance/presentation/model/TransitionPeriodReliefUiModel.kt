package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class TransitionPeriodReliefUiModel(val dateTransitionPeriodRelief: String = "",
                                         val iconTransitionPeriodRelief: String = "") : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}