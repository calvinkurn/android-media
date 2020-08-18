package com.tokopedia.shop.analytic

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.trackingoptimizer.TrackingQueue

/*
Shop Page Home Revamp
Data layer docs
https://docs.google.com/spreadsheets/d/1l91ritx5rj-RJzcTNVXnMTcOp3sWZz6O2v__nfV64Co/edit#gid=306885993
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
                                customDimensionShopPage.shopType,
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
                                customDimensionShopPage.shopType,
                                shopId
                        ))))
        sendDataLayerEvent(eventMap)
    }

    fun clickSeeAllMerchantVoucher(
            isOwner: Boolean,
            shopId: String,
            layoutId: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                "$MERCHANT_VOUCHER_CODE - $CLICK_SEE_ALL",
                "$shopId - $MERCHANT_VOUCHER - $layoutId",
                customDimensionShopPage
        )
    }

    fun clickDetailMerchantVoucher(
            isOwner: Boolean,
            shopId: String,
            layoutId: String,
            parentPosition: Int,
            position: Int,
            voucherData: MerchantVoucherViewModel,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = joinDash(HOME_TAB, MERCHANT_VOUCHER_CODE, CLICK_DETAIL)
        val eventLabel = "$shopId - $MERCHANT_VOUCHER - $layoutId - ${voucherData.voucherId}"
        val eventMap = createMap(
                PROMO_CLICK,
                getShopPageCategory(isOwner),
                eventAction,
                eventLabel,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                PROMO_CLICK to mutableMapOf(
                        PROMOTIONS to mutableListOf(
                                createVoucherItemMap(
                                        parentPosition,
                                        position,
                                        voucherData
                                )
                        )))
        sendDataLayerEvent(eventMap)
    }

    fun onImpressionVoucherItem(
            isOwner: Boolean,
            shopId: String,
            layoutId: String,
            parentPosition: Int,
            itemPosition: Int,
            voucherItem: MerchantVoucherViewModel,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = joinDash(HOME_TAB, MERCHANT_VOUCHER_CODE, IMPRESSION)
        val eventLabel = "$shopId - $MERCHANT_VOUCHER - $layoutId - ${voucherItem.voucherId}"
        val eventMap = createMap(
                PROMO_VIEW,
                getShopPageCategory(isOwner),
                eventAction,
                eventLabel,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
                PROMO_VIEW to mutableMapOf(
                        PROMOTIONS to mutableListOf(
                                createVoucherItemMap(
                                        parentPosition,
                                        itemPosition,
                                        voucherItem
                                )
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

    fun impressionLeftPlayBanner(
            shopId: String,
            positionChannel: String,
            userId: String,
            bannerId: String,
            creativeName: String,
            position: String
    ){
        val eventMap = mapOf(
                EVENT to VIEW_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_ACTION to IMPRESSION_SGC_BANNER,
                EVENT_LABEL to "view on banner play - $shopId - $position",
                USER_ID to userId,
                ECOMMERCE to mapOf(
                        PROMO_VIEW to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                ID to bannerId,
                                                NAME to PLAY_LEFT_BANNER_NAME.format(positionChannel),
                                                CREATIVE to creativeName,
                                                POSITION to positionChannel
                                        )
                                )
                        )
                )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(eventMap)
    }

    fun clickLeftPlayBanner(
            shopId: String,
            positionChannel: String,
            userId: String,
            bannerId: String,
            creativeName: String,
            position: String
    ){
        val eventMap = mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_ACTION to CLICK,
                EVENT_LABEL to "click on banner play - $shopId - $position",
                USER_ID to userId,
                ECOMMERCE to mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                ID to bannerId,
                                                NAME to PLAY_LEFT_BANNER_NAME.format(positionChannel),
                                                CREATIVE to creativeName,
                                                POSITION to positionChannel
                                        )
                                )
                        )
                )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(eventMap)
    }

    fun impressionPlayBanner(
            shopId: String,
            channelId: String,
            positionWidget: Int,
            positionChannel: String,
            autoPlay: String,
            userId: String,
            bannerId: String,
            creativeName: String

    ){
        val eventMap = mapOf(
                EVENT to VIEW_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_ACTION to IMPRESSION_SGC_CHANNEL,
                EVENT_LABEL to "view channel - $shopId - $channelId - $positionWidget - $autoPlay",
                USER_ID to userId,
                ECOMMERCE to mapOf(
                        PROMO_VIEW to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                ID to bannerId,
                                                NAME to PLAY_SGC_NAME.format(positionChannel),
                                                CREATIVE to creativeName,
                                                POSITION to positionChannel
                                        )
                                )
                        )
                )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(eventMap)
    }

    fun clickPlayBanner(
            shopId: String,
            channelId: String,
            positionWidget: Int,
            positionChannel: String,
            autoPlay: String,
            userId: String,
            bannerId: String,
            creativeName: String

    ){
        val eventMap = mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_ACTION to CLICK,
                EVENT_LABEL to "click channel - $shopId - $channelId - $positionWidget - $autoPlay",
                USER_ID to userId,
                ECOMMERCE to mapOf(
                        PROMO_CLICK to mapOf(
                                PROMOTIONS to listOf(
                                        mapOf(
                                                ID to bannerId,
                                                NAME to PLAY_SGC_NAME.format(positionChannel),
                                                CREATIVE to creativeName,
                                                POSITION to positionChannel
                                        )
                                )
                        )
                )
        ) as HashMap<String, Any>
        sendDataLayerEvent(eventMap)
    }

    fun clickSeeMorePlayCarouselBanner(shopId: String, userId: String) {
        val eventMap = mapOf(
                EVENT to CLICK_SHOP_PAGE,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_ACTION to CLICK_OTHER_CONTENT,
                EVENT_LABEL to shopId,
                USER_ID to userId
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
        return mutableMapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productDisplayedPrice),
                BRAND to shopName,
                CATEGORY to NONE,
                VARIANT to NONE,
                LIST to listEventValue,
                POSITION to horizontalPosition,
                DIMENSION_81 to customDimensionShopPage.shopType,
                DIMENSION_79 to customDimensionShopPage.shopId,
                SHOP_REF to customDimensionShopPage.shopRef
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
                DIMENSION_81 to customDimensionShopPage.shopType,
                DIMENSION_79 to customDimensionShopPage.shopId
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
}
