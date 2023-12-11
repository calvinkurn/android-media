package com.tokopedia.product.detail.view.viewholder.gwp.tracker

import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 07/12/23"
 * Project name: android-tokopedia-core
 **/

class GWPTracker(
    private val trackingQueue: TrackingQueue
) {

    fun tracking(event: GWPEvent) {
        when (event) {
            is GWPEvent.OnClickComponent -> {
                // event.data.action.navigate()
            }

            is GWPEvent.OnClickProduct -> {
                // event.data.action.navigate()
            }

            is GWPEvent.OnClickShowMore -> {
                // event.data.action.navigate()
            }

            is GWPEvent.OnCardImpress -> {
                // no-ops
            }
        }
    }

    /**
     * {
     *   "event": "promoClick",
     *   "eventAction": "click - card on gwp",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "",
     *   "trackerId": "48996",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "promotions": [
     *     {
     *       "creative_name": "{text};//list of text displayed in each card",
     *       "creative_slot": "{this is integer}",
     *       "item_id": "{offer_id}:{product_id};//list of offer_id with productID contains in each offerID",
     *       "item_name": "{offer_id} //offer_id clicked"
     *     }
     *   ],
     *   "shopId": "{shop_id}; //shop_id level hit",
     *   "userId": "{user_id}; //user_id level hit"
     * }
     */
    private fun productCardClicked(commonTracker: CommonTracker) {
        val action = "click - card on gwp"
        val event = "promoClick"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to "",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "48996",
            "productId" to commonTracker.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = commonTracker.productInfo),
            // "component" to component.getComponentData(action),
            "ecommerce" to hashMapOf(
                event to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "{text}",
                            //   "creative_slot" to component.adapterPosition,
                            "item_id" to "{offer_id}:{product_id}"
                            //  "item_name" to "keyword_count:$count"
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
