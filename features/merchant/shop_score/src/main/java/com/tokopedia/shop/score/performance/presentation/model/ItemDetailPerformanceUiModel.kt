package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ItemDetailPerformanceUiModel(
    override var titleDetailPerformance: String = "",
    override var valueDetailPerformance: String = "-",
    override var colorValueDetailPerformance: String = "",
    override var targetDetailPerformance: String = "",
    override var isDividerHide: Boolean = false,
    override var identifierDetailPerformance: String = "",
    override var parameterValueDetailPerformance: String = "",
    override var shopAge: Long = 0,
    override var shopScore: Long = -1
) : BaseShopPerformance, BaseDetailPerformanceUiModel(
    titleDetailPerformance,
    valueDetailPerformance,
    colorValueDetailPerformance,
    targetDetailPerformance,
    isDividerHide,
    identifierDetailPerformance,
    parameterValueDetailPerformance,
    shopAge,
    shopScore
) {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}