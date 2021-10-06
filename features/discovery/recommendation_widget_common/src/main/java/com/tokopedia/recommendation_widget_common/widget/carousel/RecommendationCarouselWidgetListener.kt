package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 5/3/21.
 */
interface RecommendationCarouselWidgetListener : RecomCarouselWidgetBasicListener {

    fun onRecomProductCardAddToCartNonVariant(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        quantity: Int
    )
}