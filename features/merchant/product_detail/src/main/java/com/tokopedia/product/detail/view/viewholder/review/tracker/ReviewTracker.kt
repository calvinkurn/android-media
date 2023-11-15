package com.tokopedia.product.detail.view.viewholder.review.tracker

import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 15/11/23"
 * Project name: android-tokopedia-core
 **/

/**
 * Tracker link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4379
 */
object ReviewTracker {

    /**
     * {
     *   "event": "select_content",
     *   "eventAction": "click - review chips filter",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "",
     *   "trackerId": "48619",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID} //Product ID of product displayed on PDP",
     *   "promotions": [
     *     {
     *       "creative_name": "is_active:{true/false};",
     *       "creative_slot": "position:{position}; //position of selected chips in the displayed layout",
     *       "item_id": "keyword_text:{text};",
     *       "item_name": "keyword_count:{number};"
     *     }
     *   ],
     *   "userId": "{user_id} //user_id level hit, pass null if non login"
     * }
     */
    fun onKeywordClicked(
        queueTracker: TrackingQueue,
        trackerData: CommonTracker,
        count: Int
    ) {
        val action = "click - review chips filter"
        val event = "promoClick"
        val component = trackerData.componentTracker
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to "",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "48619",
            "productId" to trackerData.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = trackerData.productInfo),
            "component" to component.getComponentData(action).orEmpty(),
            "ecommerce" to hashMapOf(
                event to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "",
                            "creative_slot" to "position:${component.adapterPosition}",
                            "item_id" to "keyword_text:${component.componentName}",
                            "item_name" to "keyword_count:$count"
                        )
                    )
                )
            ),
            "userId" to trackerData.userId
        )

        queueTracker.putEETracking(mapEvent)
    }
}
