package com.tokopedia.play.broadcaster.analytic.report

import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 15 March 2024
 */
class PlayBroadcastReportAnalyticImpl @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory
) : PlayBroadcastReportAnalytic {

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = "",
        eventCategory = "",
    )
}
