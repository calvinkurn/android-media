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

object ProductVariantTracking {
    var gtmTracker: ContextAnalytics? = null
    const val KEY_SHOP_ID = "shopId"
    const val KEY_SCREEN_NAME = "screenName"
    const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
    const val KEY_CURRENT_SITE = "currentSite"
    const val KEY_USER_ID = "userId"
    const val EVENT_OPEN_SCREEN = "openScreen"
    const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    const val EVENT_CLICK_EDIT_PRODUCT = "clickEditProduct"
    const val CAT_ADD_VARIANT_PAGE = "add variant page"
    const val CAT_ADD_VARIANT_DETAIL_PAGE = "add detail variant page"
    const val CAT_EDIT_VARIANT_PAGE = "edit variant page"
    const val CAT_EDIT_VARIANT_DETAIL_PAGE = "edit detail variant page"

    fun getTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().getGTM()
        }
        return gtmTracker!!
    }

    fun sendOpenProductVariantPage(screenName: String, isLoggedInStatus: String, userId: String, currentSite: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_OPEN_SCREEN,
                KEY_SCREEN_NAME to screenName,
                KEY_IS_LOGGED_IN_STATUS to isLoggedInStatus,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userId
        )
        getTracker().sendGeneralEvent(map)
    }

    fun sendAddProductVariantClick(action: String, label: String, shopId: String, screenName: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_CLICK_ADD_PRODUCT,
                KEY_CATEGORY to CAT_ADD_VARIANT_PAGE,
                KEY_ACTION to action,
                KEY_LABEL to label,
                KEY_SHOP_ID to shopId,
                KEY_SCREEN_NAME to screenName
        )
        getTracker().sendGeneralEvent(map)
    }

    fun sendAddProductVariantDetailClick(action: String, label: String, shopId: String, screenName: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_CLICK_ADD_PRODUCT,
                KEY_CATEGORY to CAT_ADD_VARIANT_DETAIL_PAGE,
                KEY_ACTION to action,
                KEY_LABEL to label,
                KEY_SHOP_ID to shopId,
                KEY_SCREEN_NAME to screenName
        )
        getTracker().sendGeneralEvent(map)
    }

    fun sendEditProductVariantClick(action: String, label: String, shopId: String, screenName: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_CLICK_EDIT_PRODUCT,
                KEY_CATEGORY to CAT_EDIT_VARIANT_PAGE,
                KEY_ACTION to action,
                KEY_LABEL to label,
                KEY_SHOP_ID to shopId,
                KEY_SCREEN_NAME to screenName
        )
        getTracker().sendGeneralEvent(map)
    }

    fun sendEditProductVariantDetailClick(action: String, label: String, shopId: String, screenName: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_CLICK_EDIT_PRODUCT,
                KEY_CATEGORY to CAT_EDIT_VARIANT_DETAIL_PAGE,
                KEY_ACTION to action,
                KEY_LABEL to label,
                KEY_SHOP_ID to shopId,
                KEY_SCREEN_NAME to screenName
        )
        getTracker().sendGeneralEvent(map)
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