package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionShopRecommendationUiModel(val recommendationShopList: List<ItemShopRecommendationUiModel>) : BaseShopPerformance {

    data class ItemShopRecommendationUiModel(
            val iconRecommendationUrl: String = "",
            val titleRecommendation: String = "",
            val descRecommendation: String = "",
            val appLinkRecommendation: String = "",
            val identifier: String = ""
    )

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}