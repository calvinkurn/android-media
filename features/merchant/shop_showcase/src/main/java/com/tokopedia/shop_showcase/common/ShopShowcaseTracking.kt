package com.tokopedia.shop_showcase.common

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

class ShopShowcaseTracking (context: Context) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }
    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"
    private val SHOP_ID = "shopId"
    private val SHOP_TYPE = "shopType"
    private val PAGE_TYPE = "pageType"

    private val CLICK_ETALASE = "clickEtalase"
    private val ETALASE_SETTING_PAGE = "etalase setting page"
    private val PAGE_TYPE_VALUE = "/shoppage"


    fun getDatalayer(event: String, eventCategory: String, eventAction: String,
            eventLabel: String, shopId: String, shopType: String, pageType: String
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

    fun sendScreenName() {
        val screenName = "/add etalase page - start"
        tracker.sendScreenAuthenticated(screenName)
    }

    // No 19
    fun clickBackButton(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click back",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 20
    fun clickSusun(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click susun",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 21
    fun clickSearchBar(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click search etalase",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 22
    fun clickTambahEtalase(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click add etalase",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 23
    fun clickEtalase(shopId: String, shopType: String, showcaseName: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click etalase $showcaseName",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 24
    fun clickDots(shopId: String, shopType: String, showcaseName: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click dots - etalase $showcaseName",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 25
    fun clickEditMenu(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click edit",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 26
    fun clickDeleteMenu(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click delete",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 27
    fun clickHapusDialog(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click hapus",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 28
    fun clickBatalDialog(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDatalayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click cancel",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

}

