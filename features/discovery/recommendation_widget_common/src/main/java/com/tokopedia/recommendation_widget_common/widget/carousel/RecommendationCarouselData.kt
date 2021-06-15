package com.tokopedia.recommendation_widget_common.widget.carousel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by yfsx on 5/3/21.
 */
data class RecommendationCarouselData(
        val recommendationData: RecommendationWidget = RecommendationWidget()
): ImpressHolder()