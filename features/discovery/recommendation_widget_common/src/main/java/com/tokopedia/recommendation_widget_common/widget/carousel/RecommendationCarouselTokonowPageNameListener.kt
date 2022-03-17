package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData2
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 06/10/21.
 */
interface RecommendationCarouselTokonowPageNameListener {

    fun onMiniCartUpdatedFromRecomWidget(miniCartSimplifiedData: MiniCartSimplifiedData2)

    fun onRecomTokonowAtcSuccess(message: String)

    fun onRecomTokonowAtcFailed(throwable: Throwable)

    fun onRecomTokonowAtcNeedToSendTracker(
        recommendationItem: RecommendationItem
    )

    fun onRecomTokonowDeleteNeedToSendTracker(
        recommendationItem: RecommendationItem
    )

    fun onClickItemNonLoginState()

    fun onRecomProductCardAddVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    )

}