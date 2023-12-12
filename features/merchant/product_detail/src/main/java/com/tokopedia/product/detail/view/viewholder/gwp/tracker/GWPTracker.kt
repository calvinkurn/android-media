package com.tokopedia.product.detail.view.viewholder.gwp.tracker

import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 07/12/23"
 * Project name: android-tokopedia-core
 **/

class GWPTracker(
    private val trackingQueue: TrackingQueue
) {

    fun tracking(event: GWPEvent, commonTracker: CommonTracker?) {
        val commonTracker = commonTracker ?: return

        when (event) {
            is GWPEvent.OnClickComponent,
            is GWPEvent.OnClickShowMore -> {
                // event.data.action.navigate()
            }

            is GWPEvent.OnClickProduct -> {
                // event.data.action.navigate()
            }

            is GWPEvent.OnCardImpress -> {
                trackCardImpression(event.card.trackData, commonTracker)
            }
        }
    }

    /**
     * {
     *   "event": "view_item",
     *   "eventAction": "impression - gwp card",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "text:{text showed in each project};card_count:{number};",
     *   "trackerId": "48993",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "promotions": [
     *     {
     *       "creative_name": "text:{text};//list of text displayed in each card",
     *       "creative_slot": "{slot/position}",
     *       "item_id": "offer_id:{offer_id};offer_type:{offer_type};",
     *       "item_name": "{product_id1,product_id2,product_id3};//list of offer_id with productID contains in each offerID"
     *     },
     *   ],
     *   "shopId": "{shop_id}; //shop_id level hit",
     *   "userId": "{user_id}; //user_id level hit"
     * }
     *
     */
    private fun trackCardImpression(
        cardTrackData: GWPWidgetUiModel.CardTrackData,
        commonTracker: CommonTracker
    ) {
        val action = "impression - gwp card"
        val event = "promoView"
        val eventLabel = "text:${cardTrackData.componentTitle};card_count:${cardTrackData.cardCount};"
        val itemId = "offer_id:${cardTrackData.offerId};offer_type:gwp"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to "",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "48993",
            "productId" to commonTracker.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = commonTracker.productInfo),
            "component" to cardTrackData.parentTrackData?.getComponentData(action).orEmpty(),
            "ecommerce" to hashMapOf(
                event to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to cardTrackData.allText,
                            "creative_slot" to cardTrackData.position,
                            "item_id" to itemId,
                            "item_name" to cardTrackData.productIds
                        )
                    )
                )
            ),
            "shopId" to commonTracker.shopId,
            "userId" to commonTracker.userId
        )

        trackingQueue.putEETracking(mapEvent)
    }
}
