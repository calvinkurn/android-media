package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel on 11/03/23
 */
interface RecommendationVisitable : Visitable<RecommendationTypeFactory> {
    val metadata: RecommendationWidgetMetadata
    val trackingModel: RecommendationWidgetTrackingModel

    companion object {
        fun create(
            metadata: RecommendationWidgetMetadata = RecommendationWidgetMetadata(),
            trackingModel: RecommendationWidgetTrackingModel = RecommendationWidgetTrackingModel(),
        ): RecommendationVisitable {
            return object : RecommendationVisitable {
                override val metadata: RecommendationWidgetMetadata
                    get() = metadata
                override val trackingModel: RecommendationWidgetTrackingModel
                    get() = trackingModel

                override fun type(typeFactory: RecommendationTypeFactory?): Int {
                    return 0
                }
            }
        }
    }
}
