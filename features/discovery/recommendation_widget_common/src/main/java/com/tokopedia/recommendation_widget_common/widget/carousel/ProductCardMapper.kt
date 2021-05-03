package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselProductCardDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener

/**
 * Created by yfsx on 5/3/21.
 */

fun List<RecommendationItem>.toRecomCarouselItems(hasThreeDots: Boolean = false,
                                                  listener: RecomCommonProductCardListener): List<RecomCarouselProductCardDataModel>{
    return map {
        RecomCarouselProductCardDataModel(
                productModel = it.toProductCardModel(hasThreeDots = hasThreeDots),
                recomItem = it,
                listener = listener)
    }
}