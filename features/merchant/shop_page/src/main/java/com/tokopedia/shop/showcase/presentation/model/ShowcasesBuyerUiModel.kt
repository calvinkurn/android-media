package com.tokopedia.shop.showcase.presentation.model

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcase

data class ShowcasesBuyerUiModel(
        var getFeaturedShowcase: GetFeaturedShowcase? = null,
        var allShowcaseList: List<ShopEtalaseModel> = listOf(),
        var isFeaturedShowcaseError: Boolean = false,
        var isAllShowcaseError: Boolean = false
)