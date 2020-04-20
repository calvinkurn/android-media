package com.tokopedia.product.addedit.tracking

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

val KEY_EVENT = "event"
val KEY_CATEGORY = "eventCategory"
val KEY_ACTION = "eventAction"
val KEY_LABEL = "eventLabel"

object ProductAddEditTracking {
    var gtmTracker: ContextAnalytics? = null
    const val KEY_SHOP_ID = "shopId"
    const val KEY_SCREEN_NAME = "screenName"
    const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    const val EVENT_CLICK_EDIT_PRODUCT = "clickEditProduct"
    const val CAT_ADD_PRODUCT_PAGE = "add product page"
    const val CAT_EDIT_PRODUCT_PAGE = "edit product page"
    const val CAT_DRAFT_PRODUCT_PAGE = "draft product page"

    fun getTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().getGTM()
        }
        return gtmTracker!!
    }

    fun sendAddProductClick(screen: String, shopId: String, action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
            EVENT_CLICK_ADD_PRODUCT,
            CAT_ADD_PRODUCT_PAGE,
            action,
            label,
            mapOf(KEY_SHOP_ID to shopId, KEY_SCREEN_NAME to screen))
    }

    fun sendAddProductClickWithoutScreen(shopId: String, action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_ADD_PRODUCT,
                CAT_ADD_PRODUCT_PAGE,
                action,
                label,
                mapOf(KEY_SHOP_ID to shopId))
    }

    fun sendEditProductClick(shopId: String, action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
            EVENT_CLICK_EDIT_PRODUCT,
            CAT_EDIT_PRODUCT_PAGE,
            action,
            label,
            mapOf(KEY_SHOP_ID to shopId))
    }
}

fun ContextAnalytics.sendGeneralEventCustom(event: String, category: String,
                                            action: String, label: String,
                                            customDimension: Map<String, String>) {
    sendGeneralEvent(createEventMap(event, category, action, label, customDimension))
}

fun createEventMap(event: String, category: String,
                   action: String, label: String,
                   customDimension: Map<String, String>): Map<String, String> {
    val map = mutableMapOf(
        KEY_EVENT to event,
        KEY_CATEGORY to category,
        KEY_ACTION to action, KEY_LABEL to label)
    map.putAll(customDimension)
    return map
}