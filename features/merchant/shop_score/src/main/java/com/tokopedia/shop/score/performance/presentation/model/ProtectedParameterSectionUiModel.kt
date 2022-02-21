package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class ProtectedParameterSectionUiModel(
    override val itemProtectedParameterList: List<ItemProtectedParameterUiModel> = emptyList(),
    override val titleParameterRelief: Int? = null,
    override val descParameterRelief: Int? = null,
    override val descParameterReliefBottomSheet: Int? = null,
    override val protectedParameterDaysDate: String = ""
) : BaseProtectedParameterSectionUiModel(
    itemProtectedParameterList,
    titleParameterRelief,
    descParameterRelief,
    descParameterReliefBottomSheet,
    protectedParameterDaysDate
), BaseShopPerformance {
    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}