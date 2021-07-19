package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionRMPotentialPMProUiModel(
        val potentialPMProPMBenefitList: List<ItemPMProBenefitUIModel> = listOf()
): BaseShopPerformance {

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemPMProBenefitUIModel(
            val iconPotentialPMProUrl: String = "",
            @StringRes val titlePotentialPMPro: Int? = null
    )
}