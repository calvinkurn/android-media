package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ProtectedParameterSectionUiModel(
    val itemProtectedParameterList: List<ItemProtectedParameterUiModel> = emptyList(),
    val protectedParameterDate: String = ""
) : BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class ItemProtectedParameterUiModel(
    val parameterTitle: String = ""
)