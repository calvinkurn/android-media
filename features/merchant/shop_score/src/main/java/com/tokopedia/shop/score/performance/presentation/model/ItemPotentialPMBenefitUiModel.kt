package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionPotentialPMBenefitUiModel(
        var datePotentialPMBenefit: String = "",
        var potentialPMBenefitList: List<ItemPotentialPMBenefitUIModel> = listOf()
): BaseShopPerformance {

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemPotentialPMBenefitUIModel(
            val iconPotentialPMUrl: String = "",
            val titlePotentialPMU: String = ""
    )
}