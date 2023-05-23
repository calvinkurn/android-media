package com.tokopedia.recommendation_widget_common.widget.carousel.global

import com.tokopedia.recommendation_widget_common.widget.carousel.RecomCarouselChipListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselTokonowListener
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel

/**
 * Created by frenzel on 27/03/23
 */
data class RecommendationCarouselModel(
    val recommendationVisitable: RecommendationVisitable,
    val tokonowListener: RecommendationCarouselTokonowListener? = null,
    val chipListener: RecomCarouselChipListener? = null
) : RecommendationVisitable by recommendationVisitable {
    override fun type(typeFactory: RecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    val isTokonow: Boolean = this.metadata.pageName == PAGE_NAME_TOKONOW

    companion object {
        private const val PAGE_NAME_TOKONOW = "pdp_9_tokonow"

        fun from(
            metadata: RecommendationWidgetMetadata,
            trackingModel: RecommendationWidgetTrackingModel,
        ) = RecommendationCarouselModel(
            RecommendationVisitable.create(metadata, trackingModel)
        )
    }
}
