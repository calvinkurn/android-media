package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.trackingoptimizer.TrackingQueue


class ShopPageTrackingShopPageSetting(
        trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {
    override fun clickBackArrow(isMyShop: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_BACK,
                "",
                customDimensionShopPage)
    }

//    fun clickShareButtonSellerView(customDimensionShopPage: CustomDimensionShopPage) {
//        sendGeneralEvent(CLICK_SHOP_PAGE,
//                SETTING_PAGE_SELLER,
//                CLICK_SHARE,
//                "",
//                customDimensionShopPage)
//    }

    fun clickShopDashboard(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_SHOP_DASHBOARD,
                "",
                customDimensionShopPage)
    }

    fun clickChangeShopProfile(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_CHANGE_SHOP_PROFILE,
                "",
                customDimensionShopPage)
    }

    fun clickChangeShopNote(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_CHANGE_SHOP_NOTE,
                "",
                customDimensionShopPage)
    }

    fun clickSetOpenShopTime(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_SET_OPEN_SHOP_TIME,
                "",
                customDimensionShopPage)
    }

    fun clickSeeProduct(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_SEE_PRODUCT,
                "",
                customDimensionShopPage)
    }

    fun clickAddAndEditEtalase(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_ADD_AND_EDIT_ETALASE,
                "",
                customDimensionShopPage)
    }

    fun clickPusatBantuan(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_PUSAT_BANTUAN,
                "",
                customDimensionShopPage)
    }

    fun clickPusatSeller(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_PUSAT_SELLER,
                "",
                customDimensionShopPage)
    }

    fun clickAddAndEditShopLocation(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_ADD_AND_EDIT_SHOP_LOCATION,
                "",
                customDimensionShopPage)
    }

    fun clickSetShippingService(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SETTING_PAGE_SELLER,
                CLICK_SET_SHIPPING_SERVICE,
                "",
                customDimensionShopPage)
    }
}
