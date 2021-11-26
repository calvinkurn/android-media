package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_IMPRESSION_SHOP_ALL_SHOWCASE_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SEARCH_ETALASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ECOMMERCE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FEATURED_ETALASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.POSITION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMOTIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_VIEW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SHOWCASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SHOWCASE_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
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
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for featured showcase item clicked
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_CLICK,
                eventCategory = ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                eventAction = ShopPageTrackingConstant.CLICK_FEATURED_SHOWCASE,
                eventLabel = ShopPageTrackingConstant.CLICK_FEATURED_SHOWCASE,
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                CREATIVE to featuredShowcase.name,
                                                ID to featuredShowcase.id,
                                                NAME to ShopPageTrackingConstant.FEATURED_ETALASE,
                                                POSITION to position
                                        )
                                )
                        )
                )
        ))
    }

    fun featuredShowcaseItemImpressed(
            featuredShowcase: FeaturedShowcaseUiModel,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for featured showcase item impressed
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_VIEW,
                eventCategory = ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                eventAction = ShopPageTrackingConstant.IMPRESSION_FEATURED_SHOWCASE,
                eventLabel = ShopPageTrackingConstant.IMPRESSION_FEATURED_SHOWCASE,
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
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
        ))
    }

    fun clickAllShowcaseItem(
            allShowcaseItem: ShopEtalaseUiModel,
            maxShowcaseList: Int,
            isOwner: Boolean,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for all showcase item clicked
        sendDataLayerEvent(createNewEventMap(
                eventName = PROMO_CLICK,
                eventCategory = getShopPageCategory(isOwner),
                eventAction = "$CLICK $SHOP_SHOWCASE_LIST",
                eventLabel = joinDash(CLICK, SHOP_SHOWCASE, allShowcaseItem.id, maxShowcaseList.toString()),
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                CREATIVE to allShowcaseItem.name,
                                                ID to allShowcaseItem.id,
                                                NAME to SHOP_SHOWCASE.replace(" ", "_"),
                                                POSITION to position
                                        )
                                )
                        )
                )
        ))
    }

    fun showcaseItemImpressed(
            showcaseItem: ShopEtalaseUiModel,
            position: Int,
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        // send tracker for featured showcase item impressed
        sendDataLayerEvent(createEventMap(
                eventName = PROMO_VIEW,
                eventCategory = ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                eventAction = ShopPageTrackingConstant.IMPRESSION_ALL_SHOWCASE,
                eventLabel = String.format(LABEL_IMPRESSION_SHOP_ALL_SHOWCASE_LIST, showcaseItem.id, showcaseItem.count.toString()),
                userId = userId,
                customDimensionShopPage = customDimensionShopPage,
                ecommerceMap = mapOf(
                        PROMO_VIEW to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                CREATIVE to showcaseItem.name,
                                                ID to showcaseItem.id,
                                                NAME to ShopPageTrackingConstant.ALL_SHOP_ETALASE,
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
                SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
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
                SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                USER_ID to userId
        )
        ecommerceMap?.let {
            eventMap[ECOMMERCE] = ecommerceMap
        }
        return eventMap
    }

}