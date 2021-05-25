package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionPotentialPMBenefitUiModel(
        val potentialPMBenefitList: List<ItemPotentialPMBenefitUIModel> = listOf(),
        val transitionEndDate: String = ""
): BaseShopPerformance {

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemPotentialPMBenefitUIModel(
            val iconPotentialPMUrl: String = "",
            @StringRes val titlePotentialPM: Int? = null
    )
}