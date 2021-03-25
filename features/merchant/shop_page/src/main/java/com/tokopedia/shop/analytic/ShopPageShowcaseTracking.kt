package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.showcase.presentation.model.FeaturedShowcaseUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by Rafli Syam on 24/03/2021
 */
class ShopPageShowcaseTracking(
        trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {

    fun clickFeaturedShowcaseItem(
            featuredShowcase: FeaturedShowcaseUiModel,
            isOwner: Boolean,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for featured showcase item clicked
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_CLICK,
                eventCategory = getShopPageCategory(isOwner),
                eventAction = CLICK,
                eventLabel = joinDash(FEATURED_ETALASE, customDimensionShopPage.shopId),
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf<Any>(
                                        mapOf(
                                                CREATIVE to featuredShowcase.imageUrl,
                                                ID to joinDash(featuredShowcase.id, customDimensionShopPage.shopId),
                                                NAME to joinDash(FEATURED_ETALASE, featuredShowcase.name),
                                                POSITION to position
                                        )
                                )
                        )
                )
        ))
    }

    fun featuredShowcaseItemImpressed(
            featuredShowcase: FeaturedShowcaseUiModel,
            isOwner: Boolean,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for featured showcase item impressed
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_VIEW,
                eventCategory = getShopPageCategory(isOwner),
                eventAction = IMPRESSION,
                eventLabel = joinDash(FEATURED_ETALASE, customDimensionShopPage.shopId),
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_VIEW to mapOf(
                                PROMOTIONS to mutableListOf<Any>(
                                        mapOf(
                                                CREATIVE to featuredShowcase.imageUrl,
                                                ID to joinDash(featuredShowcase.id, customDimensionShopPage.shopId),
                                                NAME to joinDash(FEATURED_ETALASE, featuredShowcase.name),
                                                POSITION to position
                                        )
                                )
                        )
                )
        ))
    }

    fun clickSearchIcon(
            isOwner: Boolean,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for search icon clicked
        sendDataLayerEvent(createEventMap(
                eventName = CLICK_SHOP_PAGE,
                eventCategory = getShopPageCategory(isOwner),
                eventAction = CLICK_SEARCH_ETALASE,
                eventLabel = customDimensionShopPage.shopId,
                userId = userId,
                customDimensionShopPage = customDimensionShopPage
        ))
    }

    fun clickAllShowcaseItem(
            allShowcaseItem: ShopEtalaseUiModel,
            isOwner: Boolean,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for all showcase item clicked
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_CLICK,
                eventCategory = getShopPageCategory(isOwner),
                eventAction = CLICK,
                eventLabel = joinDash(ALL_SHOWCASE_LIST, customDimensionShopPage.shopId),
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf<Any>(
                                        mapOf(
                                                CREATIVE to allShowcaseItem.imageUrl,
                                                ID to joinDash(allShowcaseItem.id, customDimensionShopPage.shopId),
                                                NAME to joinDash(ALL_SHOWCASE_LIST, allShowcaseItem.name),
                                                POSITION to position
                                        )
                                )
                        )
                )
        ))
    }

    fun showcaseItemImpressed(
            showcaseItem: ShopEtalaseUiModel,
            isOwner: Boolean,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for featured showcase item impressed
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_VIEW,
                eventCategory = getShopPageCategory(isOwner),
                eventAction = IMPRESSION,
                eventLabel = joinDash(ALL_SHOWCASE_LIST, customDimensionShopPage.shopId),
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_VIEW to mapOf(
                                PROMOTIONS to mutableListOf<Any>(
                                        mapOf(
                                                CREATIVE to showcaseItem.imageUrl,
                                                ID to joinDash(showcaseItem.id, customDimensionShopPage.shopId),
                                                NAME to joinDash(ALL_SHOWCASE_LIST, showcaseItem.name),
                                                POSITION to position
                                        )
                                )
                        )
                )
        ))
    }

    private fun createEventMap(
            eventName: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            userId: String,
            customDimensionShopPage: CustomDimensionShopPage,
            ecommerceMap: Map<String, Any>? = null
    ): Map<String, Any> {
        val eventMap = mutableMapOf<String, Any>(
                EVENT to eventName,
                EVENT_CATEGORY to eventCategory,
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                BUSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                PAGE_TYPE to SHOPPAGE,
                SHOP_ID to customDimensionShopPage.shopId,
                SHOP_TYPE to customDimensionShopPage.shopType,
                USER_ID to userId
        )
        ecommerceMap?.let {
            eventMap[ECOMMERCE] = ecommerceMap
        }
        return eventMap
    }

}