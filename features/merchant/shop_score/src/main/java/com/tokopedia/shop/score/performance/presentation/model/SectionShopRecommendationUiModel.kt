package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceAdapterTypeFactory

data class SectionShopRecommendationUiModel(val recommendationShopList: List<ItemShopRecommendationUiModel>) : BaseShopPerformance {

    data class ItemShopRecommendationUiModel(
            val iconRecommendationUrl: String = "",
            @StringRes val titleRecommendation: Int? = null,
            @StringRes val descRecommendation: Int? = null,
            val appLinkRecommendation: String = "")

    override fun type(typeFactory: ShopPerformanceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}