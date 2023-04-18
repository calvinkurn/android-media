package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by frenzel on 11/03/23
 */
interface RecomVisitable : Visitable<RecomTypeFactory> {
    val type: String
    val name: String
    val recomWidgetMetadata: RecomWidgetMetadata
    val recomWidgetTrackingModel: RecomWidgetTrackingModel
    val recomWidgetAnalyticListener: RecomWidgetAnalyticListener?
    var trackingQueue: TrackingQueue?
}
