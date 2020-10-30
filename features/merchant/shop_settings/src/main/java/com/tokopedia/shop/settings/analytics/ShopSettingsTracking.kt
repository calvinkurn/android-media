package com.tokopedia.shop.settings.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

object ShopSettingsTracking {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val SHOP_ID = "shopId"
    private val SHOP_TYPE = "shopType"
    private val PAGE_TYPE = "pageType"

    private val EVENT_VALUE = "clickShopPage"
    private val EVENT_CATEGORY_VALUE = "setting page - seller"
    private val PAGE_TYPE_VALUE = "/shoppage"


    private fun getDataLayer(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            shopId: String,
            shopType: String,
            pageType: String
    ): Map<String, Any> {
        return DataLayer.mapOf(
                EVENT, event,
                EVENT_CATEGORY, eventCategory,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                SHOP_ID, shopId,
                SHOP_TYPE, shopType,
                PAGE_TYPE, pageType
        )
    }


    // No. 40
    fun clickChange(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        EVENT_VALUE,
                        EVENT_CATEGORY_VALUE,
                        "click change shop information",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }


    // No. 41
    fun clickStatusToko(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        EVENT_VALUE,
                        EVENT_CATEGORY_VALUE,
                        "click shop status",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }


    // No. 42
    fun clickChangeShopLogo(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        EVENT_VALUE,
                        EVENT_CATEGORY_VALUE,
                        "click change shop picture",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }


    // No. 43
    fun clickRedirectToPusatSeller(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        EVENT_VALUE,
                        EVENT_CATEGORY_VALUE,
                        "click change domain information article",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }


    // No. 44
    fun clickCancelChangeShopName(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        EVENT_VALUE,
                        EVENT_CATEGORY_VALUE,
                        "click change domain - cancel",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }


    // No. 45
    fun clickConfirmChangeShopName(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        EVENT_VALUE,
                        EVENT_CATEGORY_VALUE,
                        "click change domain - yes",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }
}