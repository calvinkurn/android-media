package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionPotentialPMBenefitUiModel(
        val potentialPMBenefitList: List<ItemPotentialPMBenefitUIModel> = listOf()
): BaseShopPerformance {

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemPotentialPMBenefitUIModel(
            val iconPotentialPMUrl: String = "",
            @StringRes val titlePotentialPMU: Int = 0
    )
}