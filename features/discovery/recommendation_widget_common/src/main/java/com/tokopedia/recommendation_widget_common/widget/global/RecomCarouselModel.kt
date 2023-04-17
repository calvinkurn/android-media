package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselChipListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselWidgetBasicListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by frenzel on 27/03/23
 */
data class RecomCarouselModel(
    val recomVisitable: RecomVisitable,
    val tokonowListener: RecommendationCarouselTokonowListener?,
    val chipListener: RecomCarouselChipListener? = null,
    val isTokonow: Boolean = false,
) : RecomVisitable by recomVisitable {
    override fun type(typeFactory: RecomTypeFactory): Int {
        return typeFactory.type(this)
    }
}
