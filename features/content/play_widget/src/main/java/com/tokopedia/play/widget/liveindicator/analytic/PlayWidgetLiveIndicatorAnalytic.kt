package com.tokopedia.play.widget.liveindicator.analytic

import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.analytic.model.ContentEnhanceEcommerce
import com.tokopedia.play.widget.di.PlayWidgetScope
import javax.inject.Inject

@PlayWidgetScope
class PlayWidgetLiveIndicatorAnalytic @Inject internal constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory,
) {

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = "content",
        eventCategory = "product detail page - live indicator",
    )

    fun impressLiveBadge(model: Model, tag: String = "") {
        analyticManager.impressOnlyOnce("live_badge-$tag") {
            analyticManager.sendEEPromotions(
                event = Event.viewItem,
                eventAction = "view - badge",
                eventLabel = "${model.channelId} - ${model.productId} - ${model.shopId}",
                mainAppTrackerId = "49936",
                sellerAppTrackerId = "",
                customFields = mapOf(
                    Key.productId to model.productId,
                ),
                promotions = listOf(
                    ContentEnhanceEcommerce.Promotion(
                        itemId = model.channelId,
                        itemName = LIVE_INDICATOR_ITEM_NAME,
                        creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                        creativeSlot = "null",
                    )
                )
            )
        }
    }

    fun clickLiveBadge(model: Model) {
        analyticManager.sendEEPromotions(
            event = Event.selectContent,
            eventAction = "click - badge",
            eventLabel = "${model.channelId} - ${model.productId} - ${model.shopId}",
            mainAppTrackerId = "49937",
            sellerAppTrackerId = "",
            customFields = mapOf(
                Key.productId to model.productId,
            ),
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    itemId = model.channelId,
                    itemName = LIVE_INDICATOR_ITEM_NAME,
                    creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                    creativeSlot = "null",
                )
            )
        )
    }

    fun impressLiveThumbnail(model: Model, tag: String = "") {
        analyticManager.impressOnlyOnce("live_thumbnail-$tag") {
            analyticManager.sendEEPromotions(
                event = Event.viewItem,
                eventAction = "view - thumbnail",
                eventLabel = "${model.channelId} - ${model.productId} - ${model.shopId}",
                mainAppTrackerId = "49986",
                sellerAppTrackerId = "",
                customFields = mapOf(
                    Key.productId to model.productId,
                ),
                promotions = listOf(
                    ContentEnhanceEcommerce.Promotion(
                        itemId = model.channelId,
                        itemName = LIVE_INDICATOR_ITEM_NAME,
                        creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                        creativeSlot = "null",
                    )
                )
            )
        }
    }

    fun clickLiveThumbnail(model: Model) {
        analyticManager.sendEEPromotions(
            event = Event.selectContent,
            eventAction = "click - thumbnail",
            eventLabel = "${model.channelId} - ${model.productId} - ${model.shopId}",
            mainAppTrackerId = "49987",
            sellerAppTrackerId = "",
            customFields = mapOf(
                Key.productId to model.productId,
            ),
            promotions = listOf(
                ContentEnhanceEcommerce.Promotion(
                    itemId = model.channelId,
                    itemName = LIVE_INDICATOR_ITEM_NAME,
                    creativeName = LIVE_INDICATOR_CREATIVE_NAME,
                    creativeSlot = "null",
                )
            )
        )
    }
    
    companion object {
        private const val LIVE_INDICATOR_CREATIVE_NAME = "play indicator in product detail page"
        private const val LIVE_INDICATOR_ITEM_NAME = "play-indicator-pdp"
    }

    data class Model(
        val channelId: String,
        val productId: String,
        val shopId: String,
    )
}
