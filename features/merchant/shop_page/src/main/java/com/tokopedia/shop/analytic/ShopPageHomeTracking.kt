package com.tokopedia.shop.analytic

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue


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
        eventMap[ECOMMERCE] = mapOf(
                PROMO_VIEW to mapOf(
                        PROMOTIONS to listOf(createDisplayWidgetPromotionsItemMap(
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
        eventMap[ECOMMERCE] = mapOf(
                PROMO_CLICK to mapOf(
                        PROMOTIONS to listOf(createDisplayWidgetPromotionsItemMap(
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
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                joinDash(MERCHANT_VOUCHER_CODE, CLICK),
                SEE_ALL,
                customDimensionShopPage
        )
    }

    fun clickDetailMerchantVoucher(
            isOwner: Boolean,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                joinDash(MERCHANT_VOUCHER_CODE, CLICK),
                MVC_DETAIL,
                customDimensionShopPage
        )
    }

    fun onImpressionVoucherList(
            isOwner: Boolean,
            listVoucher: List<MerchantVoucherViewModel>,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = joinDash(HOME_TAB, MERCHANT_VOUCHER_CODE, IMPRESSION)
        val eventMap = createMap(
                PROMO_VIEW,
                getShopPageCategory(isOwner),
                eventAction,
                IMPRESSION_OF_USE_VOUCHER,
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mapOf(
                PROMO_VIEW to mapOf(
                        PROMOTIONS to createVoucherListMap(
                                listVoucher
                        )))
        sendDataLayerEvent(eventMap)
    }

    private fun createVoucherListMap(
            listVoucher: List<MerchantVoucherViewModel>
    ): List<Map<String, Any>> {
        return mutableListOf<Map<String, Any>>().apply {
            listVoucher.forEachIndexed { index, merchantVoucherViewModel ->
                add(createVoucherItemMap(index, merchantVoucherViewModel))
            }
        }
    }

    private fun createVoucherItemMap(index: Int, voucherData: MerchantVoucherViewModel): Map<String, Any> {
        return mutableMapOf(
                ID to voucherData.voucherId,
                NAME to PROMO_SLOT_NAME,
                CREATIVE to "",
                CREATIVE_URL to (voucherData.bannerUrl ?: ""),
                POSITION to (index + 1),
                CATEGORY to "",
                PROMO_ID to voucherData.voucherId,
                PROMO_CODE to voucherData.voucherCode
        )
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
            widgetOption: String,
            customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = joinDash(PRODUCT_LIST_IMPRESSION, HOME_TAB, layoutId, widgetName)
        val eventMap = createMap(
                PRODUCT_VIEW,
                getShopPageCategory(isOwner),
                eventAction,
                "",
                customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mapOf(
                CURRENCY_CODE to IDR,
                IMPRESSIONS to listOf(createProductItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        verticalPosition,
                        horizontalPosition,
                        widgetId,
                        widgetName,
                        widgetOption,
                        isLogin,
                        customDimensionShopPage
                )))
        sendDataLayerEvent(eventMap)
    }

    private fun createProductItemMap(
            productName: String,
            productId: String,
            productDisplayedPrice: String,
            shopName: String,
            verticalPosition: Int,
            horizontalPosition: Int,
            widgetId: String,
            widgetName: String,
            widgetOption: String,
            isLogin: Boolean,
            customDimensionShopPage: CustomDimensionShopPage
    ): Map<String, Any> {
        val featuredOrAllProductString = if (widgetId.isEmpty()) ALL_PRODUCT else HOME_FEATURED_PRODUCT
        val widgetIdEventValue = widgetId.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val widgetOptionEventValue = widgetOption.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val listEventValue = joinDash(
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
        return mapOf(
                NAME to productName,
                ID to productId,
                PRICE to formatPrice(productDisplayedPrice),
                BRAND to shopName,
                CATEGORY to "",
                VARIANT to "",
                LIST to listEventValue,
                POSITION to horizontalPosition,
                DIMENSION_81 to customDimensionShopPage.shopType,
                DIMENSION_79 to customDimensionShopPage.shopId
        )
    }

}
