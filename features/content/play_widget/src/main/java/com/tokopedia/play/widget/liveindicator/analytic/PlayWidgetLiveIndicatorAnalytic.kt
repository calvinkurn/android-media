package com.tokopedia.play.widget.liveindicator.analytic

import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.play.widget.di.PlayWidgetScope
import javax.inject.Inject

@PlayWidgetScope
internal class PlayWidgetLiveIndicatorAnalytic @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory,
) {

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = "content",
        eventCategory = "product detail page - live indicator",
    )

    fun impressLiveBadge(
        channelId: String,
        productId: String,
        shopId: String,
    ) {
        analyticManager.impressOnlyOnce("live_badge") {
            analyticManager.sendViewContent(
                eventAction = "view - badge",
                eventLabel = "$channelId - $productId - $shopId",
                mainAppTrackerId = "49936",
                sellerAppTrackerId = "",
                customFields = mapOf(
                    Key.productId to productId,
                )
            )
        }
    }

    fun clickLiveBadge(
        channelId: String,
        productId: String,
        shopId: String,
    ) {
        analyticManager.sendClickContent(
            eventAction = "click - badge",
            eventLabel = "$channelId - $productId - $shopId",
            mainAppTrackerId = "49937",
            sellerAppTrackerId = "",
            customFields = mapOf(
                Key.productId to productId,
            )
        )
    }

    fun impressLiveThumbnail(
        channelId: String,
        productId: String,
        shopId: String,
    ) {
        analyticManager.impressOnlyOnce("live_thumbnail") {
            analyticManager.sendViewContent(
                eventAction = "view - thumbnail",
                eventLabel = "$channelId - $productId - $shopId",
                mainAppTrackerId = "49986",
                sellerAppTrackerId = "",
                customFields = mapOf(
                    Key.productId to productId,
                )
            )
        }
    }

    fun clickLiveThumbnail(
        channelId: String,
        productId: String,
        shopId: String,
    ) {
        analyticManager.sendClickContent(
            eventAction = "click - thumbnail",
            eventLabel = "$channelId - $productId - $shopId",
            mainAppTrackerId = "49987",
            sellerAppTrackerId = "",
            customFields = mapOf(
                Key.productId to productId,
            )
        )
    }
}
