package com.tokopedia.tkpd.feed_component.builder

import com.tokopedia.feedplus.data.pojo.FeedTabs

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class DynamicTabsModelBuilder {

    fun buildFeedExploreTab() = FeedTabs(
        feedData = listOf(
            FeedTabs.FeedData(
                isActive = true,
                key = "feeds",
                type = "feeds",
                position = 1,
                title = "Update"
            ),
            FeedTabs.FeedData(
                isActive = true,
                key = "explore",
                type = "explore",
                position = 2,
                title = "Explore"
            )
        )
    )
}
