package com.tokopedia.feedplus.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.tooltip.FeedSearchTooltipCategory
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

    fun impressSearchTooltip(tooltipCategory: FeedSearchTooltipCategory) {
        analyticManager.sendViewContent(
            eventAction = "view - tooltip local search",
            eventLabel = getEventLabel(tooltipCategory),
            mainAppTrackerId = "50717",
        )
    }

    fun clickSearchTooltip(tooltipCategory: FeedSearchTooltipCategory) {
        analyticManager.sendClickContent(
            eventAction = "click - tooltip local search",
            eventLabel = getEventLabel(tooltipCategory),
            mainAppTrackerId = "50718",
        )
    }

    private fun getEventLabel(tooltipCategory: FeedSearchTooltipCategory): String {
        return when (tooltipCategory) {
            FeedSearchTooltipCategory.UserAffinity -> "user_affinity"
            FeedSearchTooltipCategory.Creator -> "UGC"
            FeedSearchTooltipCategory.Story -> "story"
            FeedSearchTooltipCategory.Trending -> "trending"
            FeedSearchTooltipCategory.Promo -> "promo"
            else -> ""
        }
    }
}
