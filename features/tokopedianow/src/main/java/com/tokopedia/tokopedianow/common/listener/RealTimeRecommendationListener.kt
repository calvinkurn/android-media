package com.tokopedia.tokopedianow.common.listener

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel

interface RealTimeRecommendationListener {

    fun onRecomProductCardClicked(
        position: Int,
        product: TokoNowProductCardCarouselItemUiModel
    )

    fun onAddToCartProductNonVariant(
        channelId: String,
        item: TokoNowProductCardCarouselItemUiModel,
        quantity: Int
    )

    fun onAddToCartProductVariantClick(
        position: Int,
        item: TokoNowProductCardCarouselItemUiModel
    )

    fun refreshRealTimeRecommendation(data: HomeRealTimeRecomUiModel)

    fun removeRealTimeRecommendation(data: HomeRealTimeRecomUiModel)
}
