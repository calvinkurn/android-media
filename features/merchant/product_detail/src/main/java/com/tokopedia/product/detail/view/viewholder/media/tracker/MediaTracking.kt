package com.tokopedia.product.detail.view.viewholder.media.tracker

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.track.TrackApp

/**
 * Created by yovi.putra on 3/18/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */

object MediaTracking {

    /**
     * {
     *   "event": "viewPGIris",
     *   "eventAction": "impression - overlay recomm component",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "{text in overlay recomm icon};//e.g: Produk Serupa",
     *   "trackerId": "50209",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id} //layout level attribute Component",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "shopId": "{shop_id}; //shop_id level hit",
     *   "userId": "{user_id} //user_id level hit, pass null if non login"
     * }
     */
    fun onOverlayRecommImpressed(
        title: String,
        componentTrackDataModel: ComponentTrackDataModel?,
        commonTracker: CommonTracker?
    ) {
        val common = commonTracker ?: return
        val action = "impression - overlay recomm component"

        val mapEvent = mapOf<String, Any>(
            "event" to "viewPGIris",
            "eventAction" to action,
            "eventCategory" to "product detail page",
            "eventLabel" to title,
            "trackerId" to "50209",
            "businessUnit" to "product detail page",
            "component" to componentTrackDataModel?.getComponentData(action).orEmpty(),
            "currentSite" to "tokopediamarketplace",
            "layout" to TrackingUtil.generateLayoutValue(productInfo = common.productInfo),
            "productId" to common.productId,
            "shopId" to common.shopId,
            "userId" to common.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    /**
     * {
     *   "event": "clickPG",
     *   "eventAction": "click - overlay recomm component",
     *   "eventCategory": "product detail page",
     *   "eventLabel": "{text in overlay recomm icon};//e.g: Produk Serupa",
     *   "trackerId": "50210",
     *   "businessUnit": "product detail page",
     *   "component": "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute",
     *   "currentSite": "tokopediamarketplace",
     *   "layout": "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute",
     *   "productId": "{Product ID}; //Product ID of product displayed on PDP",
     *   "shopId": "{shop_id};//shop_id level hit",
     *   "userId": "{user_id}; //user_id level hit, pass null if non login"
     * }
     */
    fun onOverlayRecommClicked(
        title: String,
        componentTrackDataModel: ComponentTrackDataModel?,
        commonTracker: CommonTracker?
    ) {
        val componentTracker = componentTrackDataModel ?: return
        val common = commonTracker ?: return
        val action = "click - overlay recomm component"
        val event = "clickPG"
        val eventLabel = "text:$title;"
        val mapEvent = hashMapOf<String, Any>(
            "event" to event,
            "eventCategory" to "product detail page",
            "eventAction" to action,
            "eventLabel" to eventLabel,
            "businessUnit" to "product detail page",
            "currentSite" to "tokopediamarketplace",
            "trackerId" to "50210",
            "productId" to common.productId,
            "layout" to TrackingUtil.generateLayoutValue(productInfo = common.productInfo),
            "component" to componentTracker.getComponentData(action),
            "shopId" to common.shopId,
            "userId" to common.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }
}
