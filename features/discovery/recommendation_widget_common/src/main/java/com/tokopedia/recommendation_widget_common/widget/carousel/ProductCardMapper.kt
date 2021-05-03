package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselProductCardDataModel

/**
 * Created by yfsx on 5/3/21.
 */

fun List<RecommendationItem>.toRecomCarouselItems(hasThreeDots: Boolean = false): List<RecomCarouselProductCardDataModel>{
    return map {
        RecomCarouselProductCardDataModel(
                productModel = it.toProductCardModel(hasThreeDots = hasThreeDots),
                recomItem = it)
    }
}