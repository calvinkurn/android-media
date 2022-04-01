package com.tokopedia.shop_showcase.shop_showcase_tab.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop_showcase.shop_showcase_tab.domain.model.ShopFeaturedShowcaseError

data class FeaturedShowcaseUiModelResponse(
    var errorResponse: ShopFeaturedShowcaseError = ShopFeaturedShowcaseError(),
    var featuredShowcaseList: List<FeaturedShowcaseUiModel> = listOf()
)

data class FeaturedShowcaseUiModel(
        var id: String = "",
        var name: String = "",
        var count: Int = 0,
        var imageUrl: String? = ""
): ImpressHolder()