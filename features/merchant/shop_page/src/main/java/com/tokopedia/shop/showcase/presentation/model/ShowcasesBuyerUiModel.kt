package com.tokopedia.shop.showcase.presentation.model

import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel

data class ShowcasesBuyerUiModel(
        var featuredShowcaseUiModelResponse: FeaturedShowcaseUiModelResponse = FeaturedShowcaseUiModelResponse(),
        var allShowcaseList: List<ShopEtalaseUiModel> = listOf(),
        var isFeaturedShowcaseError: Boolean = false,
        var isAllShowcaseError: Boolean = false
)