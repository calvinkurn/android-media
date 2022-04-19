package com.tokopedia.shop_showcase.shop_showcase_tab.analytic

import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop_showcase.common.*
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.model.FeaturedShowcaseUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

/**
 * Created by Rafli Syam on 24/03/2021
 */
class ShopShowcaseTabTracking(private val trackingQueue: TrackingQueue) {

    private fun sendDataLayerEvent(eventTracking: Map<String, Any>) {
        if (eventTracking.containsKey(ECOMMERCE)) {
            trackingQueue.putEETracking(eventTracking as HashMap<String, Any>)
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventTracking)
        }
    }

    fun sendAllTrackingQueue() {
        trackingQueue.sendAll()
    }

    fun clickFeaturedShowcaseItem(
        featuredShowcase: FeaturedShowcaseUiModel,
        position: Int,
        shopId: String,
        userId: String
    ) {
        // send tracker for featured showcase item clicked
        sendDataLayerEvent(
            createEventMap(
                eventName = PROMO_CLICK,
                eventCategory = CATEGORY_SHOP_PAGE_BUYER,
                eventAction = CLICK_FEATURED_SHOWCASE,
                eventLabel = CLICK_FEATURED_SHOWCASE,
                userId = userId,
                shopId = shopId,
                ecommerceMap = mapOf(
                    PROMO_CLICK to mapOf(
                        PROMOTIONS to listOf(
                            mapOf(
                                CREATIVE to featuredShowcase.name,
                                ID to featuredShowcase.id,
                                NAME to FEATURED_ETALASE,
                                POSITION to position
                            )
                        )
                    )
                )
            )
        )
    }

    fun featuredShowcaseItemImpressed(
        featuredShowcase: FeaturedShowcaseUiModel,
        position: Int,
        shopId: String,
        userId: String
    ) {
        // send tracker for featured showcase item impressed
        sendDataLayerEvent(
            createEventMap(
                eventName = PROMO_VIEW,
                eventCategory = CATEGORY_SHOP_PAGE_BUYER,
                eventAction = IMPRESSION_FEATURED_SHOWCASE,
                eventLabel = IMPRESSION_FEATURED_SHOWCASE,
                userId = userId,
                shopId = shopId,
                ecommerceMap = mapOf(
                    PROMO_VIEW to mapOf(
                        PROMOTIONS to mutableListOf(
                            mapOf(
                                CREATIVE to featuredShowcase.name,
                                ID to featuredShowcase.id,
                                NAME to FEATURED_ETALASE,
                                POSITION to position
                            )
                        )
                    )
                )
            )
        )
    }

    fun clickAllShowcaseItem(
            allShowcaseItem: ShopEtalaseUiModel,
            maxShowcaseList: Int,
            position: Int,
            shopId: String,
            userId: String
    ) {
        // send tracker for all showcase item clicked
        sendDataLayerEvent(createNewEventMap(
                eventName = PROMO_CLICK,
                eventCategory = CATEGORY_SHOP_PAGE_BUYER,
                eventAction = CLICK_SHOP_SHOWCASE_LIST,
                eventLabel = String.format(LABEL_CLICK_SHOP_SHOWCASE_LIST, allShowcaseItem.id, allShowcaseItem.count),
                userId = userId,
                shopId = shopId,
                ecommerceMap = mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                CREATIVE to allShowcaseItem.name,
                                                ID to allShowcaseItem.id,
                                                NAME to ALL_SHOP_ETALASE,
                                                POSITION to position
                                        )
                                )
                        )
                )
            )
        )
    }

    fun showcaseItemImpressed(
        showcaseItem: ShopEtalaseUiModel,
        position: Int,
        shopId: String,
        userId: String
    ) {
        // send tracker for featured showcase item impressed
        sendDataLayerEvent(
            createEventMap(
                eventName = PROMO_VIEW,
                eventCategory = CATEGORY_SHOP_PAGE_BUYER,
                eventAction = IMPRESSION_ALL_SHOWCASE,
                eventLabel = String.format(LABEL_IMPRESSION_SHOP_ALL_SHOWCASE_LIST, showcaseItem.id, showcaseItem.count.toString()),
                userId = userId,
                shopId = shopId,
                ecommerceMap = mapOf(
                    PROMO_VIEW to mapOf(
                        PROMOTIONS to listOf(
                            mapOf(
                                CREATIVE to showcaseItem.name,
                                ID to showcaseItem.id,
                                NAME to ALL_SHOP_ETALASE,
                                POSITION to position
                            )
                        )
                    )
                )
            )
        )
    }

    private fun createEventMap(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        userId: String,
        shopId: String,
        ecommerceMap: Map<String, Any>? = null
    ): Map<String, Any> {
        val eventMap = mutableMapOf<String, Any>(
            EVENT to eventName,
            EVENT_CATEGORY to eventCategory,
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        ecommerceMap?.let {
            eventMap[ECOMMERCE] = ecommerceMap
        }
        return eventMap
    }

    private fun createNewEventMap(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            userId: String,
            shopId: String,
            ecommerceMap: Map<String, Any>? = null
    ): Map<String, Any> {
        val eventMap = mutableMapOf<String, Any>(
                EVENT to eventName,
                EVENT_CATEGORY to eventCategory,
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                BUSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                SHOP_ID to shopId,
                USER_ID to userId
        )
        ecommerceMap?.let {
            eventMap[ECOMMERCE] = ecommerceMap
        }
        return eventMap
    }

}