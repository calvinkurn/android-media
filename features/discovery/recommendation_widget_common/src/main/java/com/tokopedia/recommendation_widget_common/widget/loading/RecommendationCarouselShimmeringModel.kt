package com.tokopedia.recommendation_widget_common.widget.loading

import com.tokopedia.recommendation_widget_common.widget.global.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata

data class RecommendationCarouselShimmeringModel(
    val recommendationVisitable: RecommendationVisitable,
): RecommendationVisitable by recommendationVisitable {

    override fun type(typeFactory: RecommendationTypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {
        fun from(
            metadata: RecommendationWidgetMetadata,
        ): RecommendationCarouselShimmeringModel =
            RecommendationCarouselShimmeringModel(
                RecommendationVisitable.create(metadata)
            )
    }
}
