package com.tokopedia.shop.score.performance.presentation.model.tablet

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory
import com.tokopedia.shop.score.performance.presentation.model.BaseShopPerformance
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel

data class ItemHeaderParameterDetailUiModel(
    val headerShopPerformanceUiModel: HeaderShopPerformanceUiModel,
    val parameterDetailList: List<BaseParameterDetail> = emptyList()
): BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}