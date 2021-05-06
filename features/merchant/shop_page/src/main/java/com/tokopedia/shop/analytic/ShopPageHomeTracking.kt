package com.tokopedia.shop.analytic

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_FIELD
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ADD
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ALL_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BOE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BO_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BRAND
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUYER_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CAMPAIGN_SEGMENTATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CATEGORY_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ACTIVATE_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ATC_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CLOSE_FILTER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CLOSE_TNC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_DEACTIVATE_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FILTER_CHIP
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FILTER_PRICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FILTER_RATING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FILTER_SHORT_BY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FOLLOW_UNFOLLOW_TNC_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_OCC_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_OK_TOASTER_NOTIFY_ME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT_LIST_TOGGLE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SEE_ALL_CAMPAIGN_NPL_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOWCASE_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_TNC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_VIEW_ALL_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_WISHLIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_URL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENCY_CODE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_38
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_40
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_45
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_79
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_80
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_81
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_82
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_83
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DISPLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ECOMMERCE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ETALASE_X
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FOLLOW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.GENERAL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_AND_BROWSE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_DISPLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_FEATURED_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IDR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_PRODUCT_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_TNC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_TOASTER_NOTIFY_ME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LOGIN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MERCHANT_VOUCHER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NONE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NON_BO_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NON_LOGIN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_SOURCE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.POSITION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCTS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_LIST_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_VIEW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMOTIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_CODE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_VIEW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.REMOVE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SCREEN_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEE_ENTRY_POINT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_REF
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.UNFOLLOW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_FINISHED_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_FINISHED_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_HOME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_NO_SEE_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_ONGOING_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_ONGOING_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SEE_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_UPCOMING_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_UPCOMING_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VERTICAL_POSITION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WIDGET_TYPE_BUY_AGAIN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WIDGET_TYPE_CAROUSELL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WISHLIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WITHOUT_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WITH_CART
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.home.view.model.NotifyMeAction
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.trackingoptimizer.TrackingQueue

/*
Data Layer Docs:

Shop Page Home Revamp
https://docs.google.com/spreadsheets/d/1l91ritx5rj-RJzcTNVXnMTcOp3sWZz6O2v__nfV64Co/edit#gid=306885993

Mvc + Shop Follower Revamp
https://mynakama.tokopedia.com/datatracker/requestdetail/690
 */

class ShopPageHomeTracking(
        trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {

    fun impressionDisplayWidget(
            isOwner: Boolean,
            shopId: String,
            layoutId: String,
            widgetName: String,
            widgetId: String,
            positionVertical: Int,
            widgetOption: String,
            destinationLink: String,
            assetUrl: String,
            positionHorizontal: Int,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventLabel = joinDash(shopId, DISPLAY_WIDGET, layoutId, widgetName)
        val eventMap = createMap(
                PROMO_VIEW,
                getShopPageCategory(isOwner),
                IMPRESSION,
                eventLabel,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                PROMO_VIEW to mutableMapOf(
                        PROMOTIONS to mutableListOf(createDisplayWidgetPromotionsItemMap(
                                widgetId,
                                positionVertical,
                                widgetName,
                                widgetOption,
                                destinationLink,
                                assetUrl,
                                positionHorizontal,
                                customDimensionShopPage.shopType.orEmpty(),
                                shopId
                        ))))
        sendDataLayerEvent(eventMap)
    }

    fun clickDisplayWidget(
            isOwner: Boolean,
            shopId: String,
            layoutId: String,
            widgetName: String,
            widgetId: String,
            positionVertical: Int,
            widgetOption: String,
            destinationLink: String,
            assetUrl: String,
            positionHorizontal: Int,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventLabel = joinDash(shopId, DISPLAY_WIDGET, layoutId, widgetName)
        val eventMap = createMap(
                PROMO_CLICK,
                getShopPageCategory(isOwner),
                CLICK,
                eventLabel,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                PROMO_CLICK to mutableMapOf(
                        PROMOTIONS to mutableListOf(createDisplayWidgetPromotionsItemMap(
                                widgetId,
                                positionVertical,
                                widgetName,
                                widgetOption,
                                destinationLink,
                                assetUrl,
                                positionHorizontal,
                                customDimensionShopPage.shopType.orEmpty(),
                                shopId
                        ))))
        sendDataLayerEvent(eventMap)
    }

    fun impressionSeeEntryPointMerchantVoucherCoupon(
            shopId: String,
            userId: String?
    ) {
        followUnfollowShop(
                VIEW_SHOP_PAGE_IRIS,
                SEE_ENTRY_POINT,
                SHOP_PAGE_LABEL + shopId,
                userId,
        )
    }

    fun impressionProductPersonalization(
            isOwner: Boolean,
            isLogin: Boolean,
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            recommendationType: String,
            userId: String,
            shopName: String,
            horizontalPosition: Int,
            widgetHeaderTitle: String,
            widgetName: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val widgetType = if(widgetName == "buy_again") {
            WIDGET_TYPE_BUY_AGAIN
        } else {
            WIDGET_TYPE_CAROUSELL
        }
        val eventAction = IMPRESSION_PRODUCT_RECOMMENDATION
        val eventLabel = joinDash(
                widgetHeaderTitle,
                widgetType
        )
        val actionFieldList = if(isLogin) {
            joinDash(
                    SHOPPAGE,
                    BUYER_RECOMMENDATION,
                    recommendationType,
                    widgetType
            )
        } else {
            joinDash(
                    SHOPPAGE,
                    NON_LOGIN,
                    BUYER_RECOMMENDATION,
                    recommendationType,
                    widgetType
            )
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                EVENT to PRODUCT_VIEW,
                EVENT_CATEGORY to getShopPageCategory(isOwner),
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                BUSINESS_UNIT to HOME_AND_BROWSE,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                SHOP_TYPE to customDimensionShopPage.shopType.orEmpty(),
                USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to mutableListOf(createProductPersonalizationItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        horizontalPosition,
                        actionFieldList
                )))
        sendDataLayerEvent(eventMap)
    }

    fun impressionProduct(
            isOwner: Boolean,
            isLogin: Boolean,
            layoutId: String,
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            shopName: String,
            verticalPosition: Int,
            horizontalPosition: Int,
            widgetId: String,
            widgetName: String,
            widgetOption: Int,
            customDimensionShopPage: CustomDimensionShopPageAttribution
    ) {
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val eventAction = joinDash(PRODUCT_LIST_IMPRESSION, HOME_TAB, layoutId, widgetNameEventValue)
        val eventMap = createMap(
                PRODUCT_VIEW,
                getShopPageCategory(isOwner),
                eventAction,
                "",
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to mutableListOf(createProductItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        verticalPosition,
                        horizontalPosition,
                        widgetId,
                        widgetNameEventValue,
                        widgetOption,
                        isLogin,
                        customDimensionShopPage
                )))
        sendDataLayerEvent(eventMap)
    }

    fun clickProduct(
            isOwner: Boolean,
            isLogin: Boolean,
            layoutId: String,
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            shopName: String,
            verticalPosition: Int,
            horizontalPosition: Int,
            widgetId: String,
            widgetName: String,
            widgetOption: Int,
            customDimensionShopPage: CustomDimensionShopPageAttribution
    ) {
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val eventAction = joinDash(CLICK_PRODUCT, HOME_TAB, layoutId, widgetNameEventValue)
        val listEventValue = createProductListValue(
                isLogin,
                verticalPosition,
                widgetId,
                widgetNameEventValue,
                widgetOption,
                customDimensionShopPage
        )
        val eventMap = createMap(
                PRODUCT_CLICK,
                getShopPageCategory(isOwner),
                eventAction,
                productId,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CLICK to mutableMapOf(
                        ACTION_FIELD to mutableMapOf(LIST to listEventValue),
                        PRODUCTS to mutableListOf(createProductItemMap(
                                productName,
                                productId,
                                productDisplayedPrice,
                                shopName,
                                verticalPosition,
                                horizontalPosition,
                                widgetId,
                                widgetNameEventValue,
                                widgetOption,
                                isLogin,
                                customDimensionShopPage
                        ))
                )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickProductPersonalization(
            isOwner: Boolean,
            isLogin: Boolean,
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            recommendationType: String,
            shopName: String,
            userId: String,
            horizontalPosition: Int,
            widgetHeaderTitle: String,
            widgetName: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val widgetType = if(widgetName == "buy_again") {
            WIDGET_TYPE_BUY_AGAIN
        } else {
            WIDGET_TYPE_CAROUSELL
        }
        val eventAction = if(isLogin) {
            CLICK_PRODUCT_RECOMMENDATION
        } else {
            joinDash(CLICK_PRODUCT_RECOMMENDATION, NON_LOGIN)
        }
        val eventLabel = joinDash(
                widgetHeaderTitle,
                widgetType
        )
        val actionFieldList = if(isLogin) {
            joinDash(
                    SHOPPAGE,
                    BUYER_RECOMMENDATION,
                    recommendationType,
                    widgetType
            )
        } else {
            joinDash(
                    SHOPPAGE,
                    NON_LOGIN,
                    BUYER_RECOMMENDATION,
                    recommendationType,
                    widgetType
            )
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                EVENT to PRODUCT_CLICK,
                EVENT_CATEGORY to getShopPageCategory(isOwner),
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                BUSINESS_UNIT to HOME_AND_BROWSE,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                PAGE_SOURCE to SCREEN_SHOP_PAGE,
                SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                SHOP_TYPE to customDimensionShopPage.shopType.orEmpty(),
                USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CLICK to mutableMapOf(
                        ACTION_FIELD to mutableMapOf(LIST to actionFieldList),
                        PRODUCTS to mutableListOf(createProductPersonalizationItemMap(
                                productName,
                                productId,
                                productDisplayedPrice,
                                shopName,
                                horizontalPosition,
                                actionFieldList
                        ))
                )
        )
        sendDataLayerEvent(eventMap)
    }

    fun addToCart(
            isOwner: Boolean,
            cartId: String,
            attribution: String,
            isLogin: Boolean,
            layoutId: String,
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            productQuantity: Int,
            shopName: String,
            verticalPosition: Int,
            widgetId: String,
            widgetName: String,
            widgetOption: Int,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val eventAction = joinDash(CLICK_ADD_TO_CART, HOME_TAB, layoutId, widgetNameEventValue)
        val eventMap = createMap(
                ADD_TO_CART,
                getShopPageCategory(isOwner),
                eventAction,
                productId,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CURRENCY_CODE to IDR,
                ADD to mutableMapOf(
                        PRODUCTS to mutableListOf(createAddToCartProductItemMap(
                                productName,
                                productId,
                                productDisplayedPrice,
                                productQuantity,
                                shopName,
                                verticalPosition,
                                widgetId,
                                widgetNameEventValue,
                                widgetOption,
                                isLogin,
                                cartId,
                                attribution,
                                customDimensionShopPage
                        ))
                )
        )
        sendDataLayerEvent(eventMap)
    }

    fun addToCartPersonalizationProduct(
            isOwner: Boolean,
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            productQuantity: Int,
            shopName: String,
            userId: String,
            widgetHeaderTitle: String,
            widgetName: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {

        val widgetType = if(widgetName == "buy_again") {
            WIDGET_TYPE_BUY_AGAIN
        } else {
            WIDGET_TYPE_CAROUSELL
        }

        val eventAction = if(widgetType == WIDGET_TYPE_CAROUSELL) {
            CLICK_ATC_RECOMMENDATION
        } else {
            CLICK_OCC_RECOMMENDATION
        }

        val eventLabel = joinDash(
                widgetHeaderTitle,
                widgetType
        )

        val eventMap: MutableMap<String, Any> = mutableMapOf(
                EVENT to ADD_TO_CART,
                EVENT_CATEGORY to getShopPageCategory(isOwner),
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                BUSINESS_UNIT to HOME_AND_BROWSE,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CURRENCY_CODE to IDR,
                ADD to mutableMapOf(
                        PRODUCTS to mutableListOf(createAddToCartProductPersonalizationItemMap(
                                productName,
                                productId,
                                productDisplayedPrice,
                                productQuantity,
                                customDimensionShopPage.shopId.orEmpty(),
                                shopName,
                                customDimensionShopPage.shopType.orEmpty()
                        ))
                )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickWishlist(
            isOwner: Boolean,
            isWishlist: Boolean,
            layoutId: String,
            isLogin: Boolean,
            widgetName: String,
            widgetId: String,
            productId: String,
            customDimensionShopPage: CustomDimensionShopPageProduct
    ) {
        val addOrRemoveEventValue = if (isWishlist) ADD else REMOVE
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val widgetIdEventValue = widgetId.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        sendGeneralEvent(
                CLICK_WISHLIST,
                getShopPageCategory(isOwner),
                "$addOrRemoveEventValue $WISHLIST - $layoutId - $widgetNameEventValue - $loginNonLoginEventValue",
                "$widgetIdEventValue - $GENERAL - $productId",
                customDimensionShopPage
        )
    }

    fun clickCta(layoutId: String,
                 widgetName: String,
                 widgetId: String,
                 appLink: String,
                 shopId: String,
                 shopType: String,
                 isOwner: Boolean) {

        val eventMap = mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to getShopPageCategory(isOwner),
                EVENT_ACTION to String.format(CLICK_VIEW_ALL_PRODUCT, layoutId, widgetName),
                EVENT_LABEL to "$widgetId - $appLink",
                SHOP_ID to shopId,
                SHOP_TYPE to shopType,
                PAGE_TYPE to SHOPPAGE
        )
        sendDataLayerEvent(eventMap)
    }

    private fun createDisplayWidgetPromotionsItemMap(
            widgetId: String,
            verticalPosition: Int,
            widgetName: String,
            widgetOption: String,
            destinationLink: String,
            assetUrl: String,
            horizontalPosition: Int,
            shopType: String,
            shopId: String
    ): Map<String, Any> {
        val nameEvent = joinDash(
                joinSpace(SHOPPAGE, HOME_DISPLAY_WIDGET),
                String.format(VERTICAL_POSITION, verticalPosition),
                widgetName,
                widgetOption

        )
        return mutableMapOf(
                ID to widgetId,
                NAME to nameEvent,
                CREATIVE to destinationLink,
                CREATIVE_URL to assetUrl,
                POSITION to horizontalPosition,
                DIMENSION_81 to shopType,
                DIMENSION_79 to shopId
        )
    }

    private fun createVoucherListMap(
            parentPosition: Int,
            listVoucher: List<MerchantVoucherViewModel>
    ): List<Map<String, Any>> {
        return mutableListOf<Map<String, Any>>().apply {
            listVoucher.forEachIndexed { index, merchantVoucherViewModel ->
                add(createVoucherItemMap(
                        parentPosition,
                        index + 1,
                        merchantVoucherViewModel
                ))
            }
        }
    }

    private fun createVoucherItemMap(
            parentPosition: Int,
            position: Int,
            voucherData: MerchantVoucherViewModel
    ): Map<String, Any> {
        val parentPositionEventValue = String.format(VERTICAL_POSITION, parentPosition)
        return mutableMapOf(
                ID to voucherData.voucherId,
                NAME to "$SHOPPAGE $HOME_TAB $MERCHANT_VOUCHER - $parentPositionEventValue",
                CREATIVE to (voucherData.voucherName ?: ""),
                CREATIVE_URL to (voucherData.bannerUrl ?: ""),
                POSITION to position,
                PROMO_ID to voucherData.voucherId,
                PROMO_CODE to voucherData.voucherCode
        )
    }

    private fun createProductPersonalizationItemMap(
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            shopName: String,
            horizontalPosition: Int,
            actionList: String
    ): Map<String, Any> {
        return mutableMapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productDisplayedPrice),
                BRAND to shopName,
                CATEGORY to NONE,
                VARIANT to NONE,
                LIST to actionList,
                POSITION to horizontalPosition,
        )
    }

    private fun createProductItemMap(
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            shopName: String,
            verticalPosition: Int,
            horizontalPosition: Int,
            widgetId: String,
            widgetNameEventValue: String,
            widgetOption: Int,
            isLogin: Boolean,
            customDimensionShopPage: CustomDimensionShopPageAttribution
    ): Map<String, Any> {
        val listEventValue = createProductListValue(
                isLogin,
                verticalPosition,
                widgetId,
                widgetNameEventValue,
                widgetOption,
                customDimensionShopPage
        )
        val boe = if (customDimensionShopPage.isFulfillmentExist == true && customDimensionShopPage.isFreeOngkirActive == true) {
            BOE
        } else if (customDimensionShopPage.isFulfillmentExist != true && customDimensionShopPage.isFreeOngkirActive == true) {
            BO_PRODUCT
        } else {
            NON_BO_PRODUCT
        }
        return mutableMapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productDisplayedPrice),
                BRAND to shopName,
                CATEGORY to NONE,
                VARIANT to NONE,
                LIST to listEventValue,
                POSITION to horizontalPosition,
                DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
                DIMENSION_79 to customDimensionShopPage.shopId.orEmpty(),
                SHOP_REF to customDimensionShopPage.shopRef.orEmpty(),
                DIMENSION_83 to boe
        )
    }

    private fun createProductListValue(
            isLogin: Boolean,
            verticalPosition: Int,
            widgetId: String,
            widgetNameEventValue: String,
            widgetOption: Int,
            customDimensionShopPage: CustomDimensionShopPage
    ): String {
        val featuredOrAllProductString = if (widgetId.isEmpty()) ALL_PRODUCT else HOME_FEATURED_PRODUCT
        val widgetIdEventValue = widgetId.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val widgetOptionEventValue = if (widgetOption == 1) WITH_CART else WITHOUT_CART
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        return joinDash(
                joinSpace(
                        SHOPPAGE,
                        HOME_TAB,
                        String.format(VERTICAL_POSITION, verticalPosition),
                        featuredOrAllProductString
                ),
                widgetIdEventValue,
                customDimensionShopPage.shopId,
                widgetNameEventValue,
                widgetOptionEventValue,
                loginNonLoginEventValue
        )
    }

    private fun createAddToCartProductPersonalizationItemMap(
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            productQuantity: Int,
            shopId: String,
            shopName: String,
            shopType: String,
    ): Map<String, Any> {

        return mutableMapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productDisplayedPrice),
                BRAND to shopName,
                CATEGORY to NONE,
                CATEGORY_ID to NONE,
                VARIANT to NONE,
                QUANTITY to productQuantity,
                DIMENSION_45 to "45",
                SHOP_ID to shopId,
                SHOP_NAME to shopName,
                SHOP_TYPE to shopType
        )
    }

    private fun createAddToCartProductItemMap(
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            productQuantity: Int,
            shopName: String,
            verticalPosition: Int,
            widgetId: String,
            widgetNameEventValue: String,
            widgetOption: Int,
            isLogin: Boolean,
            cartId: String,
            attribution: String,
            customDimensionShopPage: CustomDimensionShopPage
    ): Map<String, Any> {
        val dimension40Value = createProductListValue(
                isLogin,
                verticalPosition,
                widgetId,
                widgetNameEventValue,
                widgetOption,
                customDimensionShopPage
        )
        return mutableMapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productDisplayedPrice),
                BRAND to shopName,
                CATEGORY to NONE,
                VARIANT to NONE,
                QUANTITY to productQuantity,
                DIMENSION_80 to shopName,
                DIMENSION_82 to "",
                DIMENSION_45 to cartId,
                DIMENSION_38 to attribution,
                DIMENSION_40 to dimension40Value,
                DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
                DIMENSION_79 to customDimensionShopPage.shopId.orEmpty()
        )
    }

    fun clickMoreMenuChip(isOwner: Boolean,
                          selectedEtalaseName: String,
                          customDimensionShopPage: CustomDimensionShopPage
    ) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_SHOWCASE_LIST, String.format(ETALASE_X, selectedEtalaseName),
                customDimensionShopPage)
    }

    fun clickClearFilter(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_CLOSE_FILTER,
                "",
                customDimensionShopPage
        )
    }

    fun impressionCampaignNplWidget(
            statusCampaign: String,
            shopId: String,
            position: Int,
            isSeeCampaign: Boolean?,
            imageId: String,
            imageUrl: String,
            customDimensionShopPage: CustomDimensionShopPage,
            isOwner: Boolean
    ) {
        val trackerBannerType = getCampaignNplTrackerBannerType(statusCampaign)
        var eventLabel = joinDash(trackerBannerType, shopId, position.toString())
        isSeeCampaign?.let{
            eventLabel = if(it){
                joinDash(eventLabel, VALUE_SEE_CAMPAIGN)
            }else{
                joinDash(eventLabel, VALUE_NO_SEE_CAMPAIGN)
            }
        }
        val eventMap = createMap(
                PROMO_VIEW,
                getShopPageCategory(isOwner),
                IMPRESSION,
                eventLabel,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                PROMO_VIEW to mutableMapOf(
                        PROMOTIONS to mutableListOf(createCampaignNplWidgetItemMap(
                                imageId,
                                trackerBannerType,
                                position,
                                imageUrl
                        ))))
        sendDataLayerEvent(eventMap)
    }

    private fun createCampaignNplWidgetItemMap(
            imageId: String,
            trackerBannerType: String,
            position: Int,
            imageUrl: String
    ): Map<String, Any> {
        val nameEvent = joinDash(
                joinSpace(SHOPPAGE, VALUE_HOME, trackerBannerType),
                position.toString()
        )
        return mutableMapOf(
                ID to imageId,
                NAME to nameEvent,
                CREATIVE to "",
                CREATIVE_URL to imageUrl,
                POSITION to position
        )
    }

    fun clickTncButton(
            isOwner: Boolean,
            statusCampaign: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(CLICK_TNC, trackerCampaignType),
                "",
                customDimensionShopPage
        )
    }

    private fun getCampaignNplTrackerBannerType(statusCampaign: String): String{
        return when(statusCampaign.toLowerCase()){
            StatusCampaign.UPCOMING.statusCampaign.toLowerCase() -> {
                VALUE_UPCOMING_BANNER
            }
            StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> {
                VALUE_ONGOING_BANNER
            }
            else -> {
                VALUE_FINISHED_BANNER
            }
        }
    }

    private fun getCampaignNplTrackerCampaignType(statusCampaign: String): String{
        return when(statusCampaign.toLowerCase()){
            StatusCampaign.UPCOMING.statusCampaign.toLowerCase() -> {
                VALUE_UPCOMING_CAMPAIGN
            }
            StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> {
                VALUE_ONGOING_CAMPAIGN
            }
            else -> {
                VALUE_FINISHED_CAMPAIGN
            }
        }
    }

    fun impressionTncPage(
            isOwner: Boolean,
            statusCampaign: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
                VIEW_SHOP_PAGE_IRIS,
                getShopPageCategory(isOwner),
                String.format(IMPRESSION_TNC, trackerCampaignType),
                "",
                customDimensionShopPage
        )
    }

    fun clickCloseTncPage(
            isOwner: Boolean,
            statusCampaign: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(CLICK_CLOSE_TNC, trackerCampaignType),
                "",
                customDimensionShopPage
        )
    }

    fun clickNotifyMeButton(
            isOwner: Boolean,
            action: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = if(action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()){
            CLICK_ACTIVATE_REMINDER
        }else{
            CLICK_DEACTIVATE_REMINDER
        }
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                eventAction,
                "",
                customDimensionShopPage
        )
    }

    fun impressionToasterActivation(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                VIEW_SHOP_PAGE_IRIS,
                getShopPageCategory(isOwner),
                IMPRESSION_TOASTER_NOTIFY_ME,
                "",
                customDimensionShopPage
        )
    }

    fun toasterActivationClickOk(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                CLICK_OK_TOASTER_NOTIFY_ME,
                "",
                customDimensionShopPage
        )
    }

    fun clickCtaCampaignNplWidget(
            isOwner: Boolean,
            statusCampaign: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                String.format(CLICK_SEE_ALL_CAMPAIGN_NPL_WIDGET, trackerCampaignType),
                "",
                customDimensionShopPage
        )
    }

    fun impressionCampaignNplProduct(
            isOwner: Boolean,
            statusCampaign: String,
            productName: String,
            productId: String,
            productPrice: String,
            shopName: String,
            verticalPosition: Int,
            horizontalPosition: Int,
            isLogin: Boolean,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = joinDash(PRODUCT_LIST_IMPRESSION, HOME_TAB, getCampaignNplTrackerCampaignType(statusCampaign))
        val eventMap = createMap(
                PRODUCT_VIEW,
                getShopPageCategory(isOwner),
                eventAction,
                "",
                customDimensionShopPage
        )
        val listEventValue = createCampaignNplProductListValue(
                verticalPosition,
                statusCampaign,
                customDimensionShopPage.shopId.orEmpty(),
                isLogin
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to mutableListOf(createCampaignNplProductItemMap(
                        productName,
                        productId,
                        productPrice,
                        shopName,
                        listEventValue,
                        horizontalPosition,
                        customDimensionShopPage
                )))
        sendDataLayerEvent(eventMap)
    }

    private fun createCampaignNplProductListValue(verticalPosition: Int, statusCampaign: String, shopId: String, isLogin: Boolean): String {
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val bannerTypeEventValue = getCampaignNplTrackerBannerType(statusCampaign)
        return joinDash(
                joinSpace(SHOPPAGE, VALUE_HOME, String.format(VERTICAL_POSITION, verticalPosition)),
                bannerTypeEventValue,
                shopId,
                loginNonLoginEventValue
        )
    }

    private fun createCampaignNplProductItemMap(
            productName: String,
            productId: String,
            productPrice: String,
            shopName: String,
            listEventValue: String,
            horizontalPosition: Int,
            customDimensionShopPage: CustomDimensionShopPage
    ): Map<String, Any> {
        return mutableMapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productPrice),
                BRAND to shopName,
                CATEGORY to NONE,
                VARIANT to NONE,
                LIST to listEventValue,
                POSITION to horizontalPosition,
                DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
                DIMENSION_79 to customDimensionShopPage.shopId.orEmpty(),
                DIMENSION_40 to listEventValue
        )
    }

    fun clickCampaignNplProduct(isOwner: Boolean,
                                statusCampaign: String,
                                productName: String,
                                productId: String,
                                productPrice: String,
                                shopName: String,
                                verticalPosition: Int,
                                horizontalPosition: Int,
                                isLogin: Boolean,
                                customDimensionShopPage: CustomDimensionShopPage
    ){
        val eventAction = joinDash(CLICK_PRODUCT, HOME_TAB, getCampaignNplTrackerCampaignType(statusCampaign))
        val eventMap = createMap(
                PRODUCT_CLICK,
                getShopPageCategory(isOwner),
                eventAction,
                productId,
                customDimensionShopPage
        )
        val listEventValue = createCampaignNplProductListValue(
                verticalPosition,
                statusCampaign,
                customDimensionShopPage.shopId.orEmpty(),
                isLogin
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                CLICK to mutableMapOf(
                        ACTION_FIELD to mutableMapOf(LIST to listEventValue),
                        PRODUCTS to mutableListOf(createCampaignNplProductItemMap(
                                productName,
                                productId,
                                productPrice,
                                shopName,
                                listEventValue,
                                horizontalPosition,
                                customDimensionShopPage
                        ))
                )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickProductListToggle(
            productListName: String,
            isMyShop: Boolean,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isMyShop),
                CLICK_PRODUCT_LIST_TOGGLE,
                productListName,
                customDimensionShopPage
        )
    }

    fun clickFilterChips(productListName: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_FILTER_CHIP,
                productListName,
                customDimensionShopPage
        )
    }

    fun clickFilterSortBy(productListName: String, sortBy: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_FILTER_SHORT_BY + sortBy,
                productListName,
                customDimensionShopPage
        )
    }

    fun clickFilterPrice(productListName: String, min: String, max: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                String.format(CLICK_FILTER_PRICE, min, max),
                productListName,
                customDimensionShopPage
        )
    }

    fun clickFilterRating(productListName: String, rating: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
                CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                CLICK_FILTER_RATING + rating,
                productListName,
                customDimensionShopPage
        )
    }

    fun clickNotifyMeNplFollowerButton(isOwner: Boolean, action: String, userId: String, customDimensionShopPage: CustomDimensionShopPage) {
        val eventAction = if(action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()){
            "$CLICK_ACTIVATE_REMINDER - $CAMPAIGN_SEGMENTATION"
        }else{
            "$CLICK_DEACTIVATE_REMINDER - $CAMPAIGN_SEGMENTATION"
        }
        sendGeneralEventNplFollower(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                eventAction,
                "",
                PHYSICAL_GOODS,
                TOKOPEDIA_MARKETPLACE,
                userId,
                customDimensionShopPage
        )
    }

    fun clickTncBottomSheetFollowButtonNplFollower(
            isOwner: Boolean,
            isFollowShop: Boolean,
            shopId: String,
            userId: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = String.format(CLICK_FOLLOW_UNFOLLOW_TNC_PAGE, FOLLOW.takeIf { isFollowShop }
                ?: UNFOLLOW)
        sendGeneralEventNplFollower(
                CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                eventAction,
                shopId,
                PHYSICAL_GOODS,
                TOKOPEDIA_MARKETPLACE,
                userId,
                customDimensionShopPage
        )
    }
}
