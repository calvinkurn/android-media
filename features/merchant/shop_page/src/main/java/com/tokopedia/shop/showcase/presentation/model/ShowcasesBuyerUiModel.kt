package com.tokopedia.shop.showcase.presentation.model

import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.showcase.domain.model.ShopFeaturedShowcaseError

data class ShowcasesBuyerUiModel(
        var getFeaturedShowcaseErrorResponse: ShopFeaturedShowcaseError = ShopFeaturedShowcaseError(),
        var featuredShowcaseList: List<FeaturedShowcaseUiModel> = listOf(),
        var allShowcaseList: List<ShopEtalaseUiModel> = listOf(),
        var isFeaturedShowcaseError: Boolean = false,
        var isAllShowcaseError: Boolean = false
)