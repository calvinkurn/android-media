package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 06/10/21.
 */
interface RecommendationCarouselTokonowListener {

    fun onRecomProductCardAddToCartNonVariant(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        quantity: Int
    )

    fun onRecomProductCardAddVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    )
}