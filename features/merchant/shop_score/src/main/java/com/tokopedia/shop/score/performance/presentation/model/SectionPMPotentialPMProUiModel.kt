package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

class SectionPMPotentialPMProUiModel(
    val potentialPMProPMBenefitList: List<ItemPMProBenefitUIModel>? = listOf()
) : BaseShopPerformance {

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemPMProBenefitUIModel(
        override val iconUrl: String = "",
        @StringRes override val titleResources: Int? = null
    ) : ItemParentBenefitUiModel()
}