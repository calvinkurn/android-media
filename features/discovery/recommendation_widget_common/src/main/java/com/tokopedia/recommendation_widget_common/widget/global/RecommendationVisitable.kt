package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by frenzel on 11/03/23
 */
interface RecommendationVisitable : Visitable<RecommendationTypeFactory> {
    val metadata: RecommendationWidgetMetadata
    val trackingModel: RecommendationWidgetTrackingModel
    val analyticListener: RecommendationWidgetAnalyticListener?
    var trackingQueue: TrackingQueue?
}
