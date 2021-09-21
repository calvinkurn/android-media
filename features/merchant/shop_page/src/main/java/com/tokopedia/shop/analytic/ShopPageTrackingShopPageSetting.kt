package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_AND_EDIT_ETALASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_AND_EDIT_SHOP_LOCATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_BACK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CHANGE_SHOP_NOTE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CHANGE_SHOP_PROFILE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PUSAT_BANTUAN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PUSAT_SELLER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SEE_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SET_OPEN_SHOP_TIME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SET_SHIPPING_SERVICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_DASHBOARD
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SETTING_PAGE_SELLER
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
