package com.tokopedia.feedplus.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 29 April 2024
 */
class FeedTooltipAnalytics @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory,
){

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.unifiedFeed,
    )

    /** JOE TODO: determine eventLabel possible values */
    fun impressSearchTooltip() {
        analyticManager.sendViewContent(
            eventAction = "view - tooltip local search",
            eventLabel = "",
            mainAppTrackerId = "50717",
        )
    }

    fun clickSearchTooltip() {
        analyticManager.sendClickContent(
            eventAction = "click - tooltip local search",
            eventLabel = "",
            mainAppTrackerId = "50718",
        )
    }
}
