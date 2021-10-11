package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemDetailPerformanceUiModel(
    var titleDetailPerformance: String = "",
    var valueDetailPerformance: String = "-",
    var colorValueDetailPerformance: String = "",
    var targetDetailPerformance: String = "",
    var isDividerHide: Boolean = false,
    var identifierDetailPerformance: String = "",
    var parameterValueDetailPerformance: String = "",
    var shopAge: Long = 0,
    var shopScore: Long = -1
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}