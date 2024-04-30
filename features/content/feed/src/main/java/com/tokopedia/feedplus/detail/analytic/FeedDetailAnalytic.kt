package com.tokopedia.feedplus.detail.analytic

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 30 April 2024
 */
class FeedDetailAnalytic @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory
) {

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.unifiedFeed,
    )

    fun clickSearchBar() {
        /** JOE TODO: eventLabel & mainAppTrackerId TBD (2 May 2024) */
        analyticManager.sendClickContent(
            eventAction = "click - search bar",
            eventLabel = "",
            mainAppTrackerId = "",
        )
    }
}
