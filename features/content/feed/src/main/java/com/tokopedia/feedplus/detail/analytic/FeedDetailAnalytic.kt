package com.tokopedia.feedplus.detail.analytic

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.analytic.model.ContentEnhanceEcommerce
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 30 April 2024
 */
class FeedDetailAnalytic @Inject constructor(
    private val userSession: UserSessionInterface,
    analyticManagerFactory: ContentAnalyticManager.Factory
) {

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.unifiedFeed,
    )

    fun impressSearchBar() {
        analyticManager.sendEEPromotions(
            event = "view_item",
            eventAction = "view - search bar",
            eventLabel = "cdp",
            mainAppTrackerId = "50786",
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    creativeName = "search bar in cdp",
                    creativeSlot = "0",
                    itemId = userSession.userId,
                    itemName = "search-bar-cdp"
                )
            )
        )
    }

    fun clickSearchBar() {
        analyticManager.sendEEPromotions(
            event = "select_content",
            eventAction = "click - search bar",
            eventLabel = "cdp",
            mainAppTrackerId = "50787",
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    creativeName = "search bar in cdp",
                    creativeSlot = "0",
                    itemId = userSession.userId,
                    itemName = "search-bar-cdp"
                )
            )
        )
    }
}
