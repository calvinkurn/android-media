package com.tokopedia.tokopedianow.common.listener

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel

interface RealTimeRecommendationListener {

    fun onRecomProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    )

    fun onAddToCartProductNonVariant(
        channelId: String,
        item: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    )

    fun onAddToCartProductVariantClick(
        position: Int,
        item: ProductCardCompactCarouselItemUiModel
    )

    fun refreshRealTimeRecommendation(data: HomeRealTimeRecomUiModel)

    fun removeRealTimeRecommendation(data: HomeRealTimeRecomUiModel)
}
