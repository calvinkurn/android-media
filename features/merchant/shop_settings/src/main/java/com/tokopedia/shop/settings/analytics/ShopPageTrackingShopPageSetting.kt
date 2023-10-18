package com.tokopedia.shop.settings.analytics

import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_ADD_AND_EDIT_ETALASE
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_ADD_AND_EDIT_SHOP_LOCATION
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_BACK
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_CHANGE_SHOP_NOTE
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_CHANGE_SHOP_PROFILE
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_PUSAT_BANTUAN
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_PUSAT_SELLER
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_SEE_PRODUCT
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_SET_OPEN_SHOP_TIME
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_SET_SHIPPING_SERVICE
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.CLICK_SHOP_DASHBOARD
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.EVENT_CATEGORY_VALUE
import com.tokopedia.shop.settings.analytics.ShopSettingsTrackingConstant.EVENT_VALUE
import com.tokopedia.shop.settings.analytics.model.CustomDimensionShopPageSetting
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

object ShopPageTrackingShopPageSetting {
    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private fun sendGeneralEvent(
        event: String,
        category: String,
        action: String,
        label: String,
        customDimensionShopPage: CustomDimensionShopPageSetting?
    ) {
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        tracker.sendGeneralEvent(eventMap)
    }

    private fun createMap(
        event: String,
        category: String,
        action: String,
        label: String,
        customDimensionShopPage: CustomDimensionShopPageSetting?
    ): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ShopSettingsTrackingConstant.EVENT] = event
        eventMap[ShopSettingsTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ShopSettingsTrackingConstant.EVENT_ACTION] = action
        eventMap[ShopSettingsTrackingConstant.EVENT_LABEL] = label
        if (customDimensionShopPage != null) {
            eventMap[ShopSettingsTrackingConstant.SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
            eventMap[ShopSettingsTrackingConstant.SHOP_TYPE] = customDimensionShopPage.shopType.orEmpty()
            eventMap[ShopSettingsTrackingConstant.PAGE_TYPE] = ShopSettingsTrackingConstant.PAGE_TYPE_VALUE
        }
        return eventMap
    }

    fun clickBackArrow(customDimensionShopPage: CustomDimensionShopPageSetting?) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_BACK,
            "",
            customDimensionShopPage
        )
    }

    fun clickShopDashboard(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_SHOP_DASHBOARD,
            "",
            customDimensionShopPage
        )
    }

    fun clickChangeShopProfile(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_CHANGE_SHOP_PROFILE,
            "",
            customDimensionShopPage
        )
    }

    fun clickChangeShopNote(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_CHANGE_SHOP_NOTE,
            "",
            customDimensionShopPage
        )
    }

    fun clickSetOpenShopTime(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_SET_OPEN_SHOP_TIME,
            "",
            customDimensionShopPage
        )
    }

    fun clickSeeProduct(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_SEE_PRODUCT,
            "",
            customDimensionShopPage
        )
    }

    fun clickAddAndEditEtalase(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_ADD_AND_EDIT_ETALASE,
            "",
            customDimensionShopPage
        )
    }

    fun clickPusatBantuan(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_PUSAT_BANTUAN,
            "",
            customDimensionShopPage
        )
    }

    fun clickPusatSeller(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_PUSAT_SELLER,
            "",
            customDimensionShopPage
        )
    }

    fun clickAddAndEditShopLocation(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_ADD_AND_EDIT_SHOP_LOCATION,
            "",
            customDimensionShopPage
        )
    }

    fun clickSetShippingService(customDimensionShopPage: CustomDimensionShopPageSetting) {
        sendGeneralEvent(
            EVENT_VALUE,
            EVENT_CATEGORY_VALUE,
            CLICK_SET_SHIPPING_SERVICE,
            "",
            customDimensionShopPage
        )
    }
}
