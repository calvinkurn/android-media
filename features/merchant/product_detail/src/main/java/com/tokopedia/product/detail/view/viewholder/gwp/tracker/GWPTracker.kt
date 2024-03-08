package com.tokopedia.product.detail.view.viewholder.gwp.tracker

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by yovi.putra on 07/12/23"
 * Project name: android-tokopedia-core
 **/

/**
 * Tracker link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4406
 */
object GWPTracker {

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
    fun onCardImpression(
        trackingQueue: TrackingQueue,
        cardTrackData: GWPWidgetUiModel.CardTrackData,
        commonTracker: CommonTracker?
    ) {
        val common = commonTracker ?: return
        val action = "impression - gwp card"
        val event = "promoView"
        val eventLabel = "text:${cardTrackData.componentTitle};card_count:${cardTrackData.cardCount};"
        val itemId = "offer_id:${cardTrackData.offerId};offer_type:gwp"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to eventLabel,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "48993",
            "productId" to common.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = common.productInfo),
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
            "shopId" to common.shopId,
            "userId" to common.userId
        )

        trackingQueue.putEETracking(mapEvent)
    }

    /**
     * {
     *   "event": "select_content",
     *   "eventAction": "click - card on gwp",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "text:{text showed in each project};//for text, e.g:  belanja untuk dapat hadiah",
     *   "trackerId": "48996",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "promotions": [
     *     {
     *       "creative_name": "text:{text};/text displayed in clicked card",
     *       "creative_slot": "{slot/position}",
     *       "item_id": "offer_id_clicked:{offer_id};offer_type:{offer_type};",
     *       "item_name": "{product_id1,product_id2,product_id3};//list of product_id with productID contains in each offerID clicked"
     *     }
     *   ],
     *   "shopId": "{shop_id}; //shop_id level hit",
     *   "userId": "{user_id}; //user_id level hit"
     * }
     *
     */
    fun onCardClicked(
        trackingQueue: TrackingQueue,
        cardTrackData: GWPWidgetUiModel.CardTrackData,
        commonTracker: CommonTracker?
    ) {
        val common = commonTracker ?: return
        val action = "click - card on gwp"
        val event = "promoClick"
        val eventLabel = "text:${cardTrackData.componentTitle};"
        val itemId = "offer_id_clicked:${cardTrackData.offerId};offer_type:gwp"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to eventLabel,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "48996",
            "productId" to common.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = common.productInfo),
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
            "shopId" to common.shopId,
            "userId" to common.userId
        )

        trackingQueue.putEETracking(mapEvent)
    }

    /**
     * {
     *   "event": "clickPG",
     *   "eventAction": "click - other offer on gwp",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "text:{text showed in project};// e.g:  belanja untuk dapat hadiah",
     *   "trackerId": "48997",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "shopId": "{shop_id}; //shop_id level hit",
     *   "userId": "{user_id}; //user_id level hit"
     * }
     */
    fun onShowMore(
        title: String,
        componentTrackDataModel: ComponentTrackDataModel?,
        commonTracker: CommonTracker?
    ) {
        val common = commonTracker ?: return
        val action = "click - other offer on gwp"
        val event = "clickPG"
        val eventLabel = "text:$title;"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to eventLabel,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "48997",
            "productId" to common.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = common.productInfo),
            "component" to componentTrackDataModel?.getComponentData(action).orEmpty(),
            "shopId" to common.shopId,
            "userId" to common.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
