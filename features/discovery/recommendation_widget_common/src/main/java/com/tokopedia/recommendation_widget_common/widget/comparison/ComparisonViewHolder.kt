package com.tokopedia.recommendation_widget_common.widget.comparison

import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

interface ComparisonViewHolder {

    fun bind(
        comparisonModel: ComparisonModel,
        comparisonListModel: ComparisonListModel,
        comparisonWidgetInterface: ComparisonWidgetInterface,
        recommendationTrackingModel: RecommendationTrackingModel,
        trackingQueue: TrackingQueue?,
        userSession: UserSessionInterface,
    )
}
