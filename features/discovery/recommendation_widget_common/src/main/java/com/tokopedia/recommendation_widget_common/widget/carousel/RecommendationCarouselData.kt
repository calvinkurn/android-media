package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by yfsx on 5/3/21.
 */
data class RecommendationCarouselData(
        val recommendationData: RecommendationWidget = RecommendationWidget(),
        val state: Int = 0
): ImpressHolder() {
    companion object {
        const val STATE_LOADING: Int = 0
        const val STATE_READY: Int = 1
        const val STATE_FAILED: Int = -1
    }
}