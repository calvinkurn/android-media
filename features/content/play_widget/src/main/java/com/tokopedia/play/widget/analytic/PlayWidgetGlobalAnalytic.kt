package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.analytic.list.jumbo.PlayWidgetInListJumboAnalyticListener
import com.tokopedia.play.widget.analytic.list.large.PlayWidgetInListLargeAnalyticListener
import com.tokopedia.play.widget.analytic.list.medium.PlayWidgetInListMediumAnalyticListener
import com.tokopedia.play.widget.analytic.list.small.PlayWidgetInListSmallAnalyticListener
import com.tokopedia.play.widget.analytic.global.PlayWidgetJumboGlobalAnalytic
import com.tokopedia.play.widget.analytic.global.PlayWidgetLargeGlobalAnalytic
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetAnalyticModel
import com.tokopedia.play.widget.analytic.global.PlayWidgetMediumGlobalAnalytic
import com.tokopedia.play.widget.analytic.global.PlayWidgetSmallGlobalAnalytic
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetGlobalAnalytic @AssistedInject constructor(
    @Assisted private val model: PlayWidgetAnalyticModel,
    @Assisted private val trackingQueue: TrackingQueue,
    private val smallAnalytic: PlayWidgetSmallGlobalAnalytic.Factory,
    private val mediumAnalytic: PlayWidgetMediumGlobalAnalytic.Factory,
    private val largeAnalytic: PlayWidgetLargeGlobalAnalytic.Factory,
    private val jumboAnalytic: PlayWidgetJumboGlobalAnalytic.Factory,
) : PlayWidgetInListAnalyticListener,
    PlayWidgetInListSmallAnalyticListener by smallAnalytic.create(model, trackingQueue),
    PlayWidgetInListMediumAnalyticListener by mediumAnalytic.create(model, trackingQueue),
    PlayWidgetInListLargeAnalyticListener by largeAnalytic.create(model, trackingQueue),
    PlayWidgetInListJumboAnalyticListener by jumboAnalytic.create(model, trackingQueue)
{

    @AssistedFactory
    interface Factory {
        fun create(
            model: PlayWidgetAnalyticModel,
            trackingQueue: TrackingQueue,
        ): PlayWidgetGlobalAnalytic
    }
}