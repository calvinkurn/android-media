package com.tokopedia.product.detail.view.viewholder.promo_price.tracker

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.trackingoptimizer.TrackingQueue

object PromoPriceTracker {
    /**
     * {
     *   "event": "select_content",
     *   "eventAction": "click - promo price component",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "promo_name:{promo_name};//promo name e.g: Diskon 200rb; Cashback 300rb;",
     *   "trackerId": "49311",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "promotions": [
     *     {
     *       "creative_name": "promo_list:{promo_list};// promo name, separate with \";\" e.g: \"Diskon 200rb; Cashback 300rb\"",
     *       "creative_slot": "position:{position}; //position promolist from left to right",
     *       "dimension79": "{shop_id} //shop_id level hit",
     *       "item_id": "promo_id:{promo_id};// promo ID (displayed dari promo Diskon 200rb s/d 300rb) , separate with \",\"",
     *       "item_name": "default_price:{default_price, e.g.Rp. 11000000}; slash_price:{slash_price from slash_pricefmt, e.g. 9500000}; coupon_price:{coupon_price, e.g. 9500000};"
     *     }
     *   ],
     *   "shopId": "{shop_id}; //shop_id level hit",
     *   "userId": "{user_id}; //user_id level hit"
     * }
     */
    fun onPromoPriceClicked(
        queueTracker: TrackingQueue,
        subtitle: String,
        defaultPriceFmt: String,
        slashPriceFmt: String,
        promoPriceFmt: String,
        promoId: List<String>,
        layoutData: ComponentTrackDataModel?,
        commonTracker: CommonTracker?
    ) {
        layoutData ?: return
        commonTracker ?: return
        val action = "click - review chips filter"
        val event = "promoClick"
        val itemName =
            "default_price:$defaultPriceFmt;slash_price:$slashPriceFmt;coupon_price:$promoPriceFmt"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to "click - promo price component",
            "eventLabel" to "promo_name:$subtitle",
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "49311",
            "productId" to commonTracker.productId,
            "shopId" to commonTracker.shopId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = commonTracker.productInfo),
            "component" to layoutData.getComponentData(action),
            "ecommerce" to hashMapOf(
                event to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to "promo_list:$subtitle",
                            "creative_slot" to "position:${layoutData.adapterPosition}",
                            "item_id" to "promo_id:${
                                promoId.joinToString(
                                    separator = ",",
                                    postfix = ";"
                                )
                            }",
                            "item_name" to itemName
                        )
                    )
                )
            ),
            "userId" to commonTracker.userId
        )
        queueTracker.putEETracking(mapEvent)
    }
}
