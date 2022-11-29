package com.tokopedia.tokopedianow.common.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel

interface RealTimeRecommendationListener {

    fun onRecomProductCardClicked(
        recomItem: RecommendationItem,
        headerName: String,
        position: String,
        applink: String
    )

    fun onAddToCartProductNonVariant(
        channelId: String,
        recomItem: RecommendationItem,
        quantity: Int,
        headerName: String,
        position: String
    )

    fun onAddToCartProductVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    )

    fun refreshRealTimeRecommendation(data: HomeRealTimeRecomUiModel)

    fun removeRealTimeRecommendation(data: HomeRealTimeRecomUiModel)
}
