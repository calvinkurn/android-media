package com.tokopedia.play.widget.liveindicator.analytic

import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.analytic.model.ContentEnhanceEcommerce
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
        tag: String = "",
    ) {
        analyticManager.impressOnlyOnce("live_badge-$tag") {
            analyticManager.sendEEPromotions(
                event = Event.viewItem,
                eventAction = "view - badge",
                eventLabel = "$channelId - $productId - $shopId",
                mainAppTrackerId = "49936",
                sellerAppTrackerId = "",
                customFields = mapOf(
                    Key.productId to productId,
                ),
                promotions = listOf(
                    ContentEnhanceEcommerce.Promotion(
                        itemId = channelId,
                        itemName = LIVE_INDICATOR_ITEM_NAME,
                        creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                        creativeSlot = "0",
                    )
                )
            )
        }
    }

    fun clickLiveBadge(
        channelId: String,
        productId: String,
        shopId: String,
    ) {
        analyticManager.sendEEPromotions(
            event = Event.selectContent,
            eventAction = "click - badge",
            eventLabel = "$channelId - $productId - $shopId",
            mainAppTrackerId = "49937",
            sellerAppTrackerId = "",
            customFields = mapOf(
                Key.productId to productId,
            ),
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    itemId = channelId,
                    itemName = LIVE_INDICATOR_ITEM_NAME,
                    creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                    creativeSlot = "0",
                )
            )
        )
    }

    fun impressLiveThumbnail(
        channelId: String,
        productId: String,
        shopId: String,
        tag: String = "",
    ) {
        analyticManager.impressOnlyOnce("live_thumbnail-$tag") {
            analyticManager.sendEEPromotions(
                event = Event.viewItem,
                eventAction = "view - thumbnail",
                eventLabel = "$channelId - $productId - $shopId",
                mainAppTrackerId = "49986",
                sellerAppTrackerId = "",
                customFields = mapOf(
                    Key.productId to productId,
                ),
                promotions = listOf(
                    ContentEnhanceEcommerce.Promotion(
                        itemId = channelId,
                        itemName = LIVE_INDICATOR_ITEM_NAME,
                        creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                        creativeSlot = "0",
                    )
                )
            )
        }
    }

    fun clickLiveThumbnail(
        channelId: String,
        productId: String,
        shopId: String,
    ) {
        analyticManager.sendEEPromotions(
            event = Event.selectContent,
            eventAction = "click - thumbnail",
            eventLabel = "$channelId - $productId - $shopId",
            mainAppTrackerId = "49987",
            sellerAppTrackerId = "",
            customFields = mapOf(
                Key.productId to productId,
            ),
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    itemId = channelId,
                    itemName = LIVE_INDICATOR_ITEM_NAME,
                    creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                    creativeSlot = "0",
                )
            )
        )
    }
    
    companion object {
        private const val LIVE_INDICATOR_CREATIVE_NAME = "play indicator in product detail page"
        private const val LIVE_INDICATOR_ITEM_NAME = "play-indicator-pdp"
    }
}
