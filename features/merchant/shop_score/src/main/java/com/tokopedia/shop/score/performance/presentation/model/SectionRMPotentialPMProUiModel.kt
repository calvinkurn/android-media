package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionRMPotentialPMProUiModel(
    val potentialPMProPMBenefitList: List<ItemPMProBenefitUIModel>? = emptyList()
) : BaseShopPerformance {

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemPMProBenefitUIModel(
        override val iconUrl: String = "",
        @StringRes override val titleResources: Int? = null
    ) : ItemParentBenefitUiModel()
}