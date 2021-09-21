package com.tokopedia.shop.showcase.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.showcase.domain.model.ShopFeaturedShowcaseError

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