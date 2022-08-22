package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.BUSINESS_UNIT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.BUSINESS_UNIT_CAPITALIZED_WORD
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CURRENT_SITE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_EDIT_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

val KEY_EVENT = "event"
val KEY_CATEGORY = "eventCategory"
val KEY_ACTION = "eventAction"
val KEY_LABEL = "eventLabel"
val KEY_BUSINESS_UNIT = "businessUnit"
val KEY_CURRENT_SITE = "currentSite"

object ProductAddEditTracking {
    var gtmTracker: ContextAnalytics? = null
    const val KEY_SHOP_ID = "shopId"
    const val KEY_TRACKER_ID = "trackerId"
    const val KEY_SCREEN_NAME = "screenName"
    const val KEY_USER_ID = "userId"
    const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    const val EVENT_VIEW_ADD_PRODUCT = "viewAddProductIris"
    const val EVENT_CLICK_EDIT_PRODUCT = "clickEditProduct"
    const val EVENT_VIEW_EDIT_PRODUCT = "viewEditProductIris"
    const val EVENT_SERVER_ERROR = "serverError"
    const val CAT_ADD_PRODUCT_PAGE = "add product page"
    const val CAT_EDIT_PRODUCT_PAGE = "edit product page"
    const val CAT_DRAFT_PRODUCT_PAGE = "draft product page"
    const val CURRENT_SITE = "tokopediaseller"
    const val BUSINESS_UNIT = "physical goods"
    const val BUSINESS_UNIT_CAPITALIZED_WORD = "Physical Goods"
    const val EVENT_VIEW_PG_IRIS = "viewPGIris"
    const val EVENT_CLICK_PG = "clickPG"

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

    fun sendAddProductClickWithoutScreen(userId: String, action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_ADD_PRODUCT,
                CAT_ADD_PRODUCT_PAGE,
                action,
                label,
                mapOf(KEY_USER_ID to userId, KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                        KEY_CURRENT_SITE to CURRENT_SITE))
    }

    fun sendAddProductClickWithoutScreenAndUserId(action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
            EVENT_CLICK_ADD_PRODUCT,
            CAT_ADD_PRODUCT_PAGE,
            action,
            label,
            mapOf(KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE))
    }

    fun sendAddProductImpression(userId: String, action: String, label: String = "") {
        val map = mapOf(
                KEY_EVENT to EVENT_VIEW_ADD_PRODUCT,
                KEY_CATEGORY to CAT_ADD_PRODUCT_PAGE,
                KEY_ACTION to action,
                KEY_LABEL to label,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_USER_ID to userId
        )
        ProductVariantTracking.getTracker().sendGeneralEvent(map)
    }

    fun sendAddProductUpload(category: String, userId: String, label: String = "") {
        val map = mapOf(
                KEY_EVENT to EVENT_SERVER_ERROR,
                KEY_CATEGORY to category,
                KEY_ACTION to "",
                KEY_LABEL to label,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_USER_ID to userId
        )
        ProductVariantTracking.getTracker().sendGeneralEvent(map)
    }

    fun sendEditProductClick(shopId: String, action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_EDIT_PRODUCT,
                CAT_EDIT_PRODUCT_PAGE,
                action,
                label,
                mapOf(KEY_SHOP_ID to shopId))
    }

    fun sendEditProductClickWithoutScreen(userId: String, action: String, label: String = "") {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_EDIT_PRODUCT,
                CAT_EDIT_PRODUCT_PAGE,
                action,
                label,
                mapOf(KEY_USER_ID to userId, KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                        KEY_CURRENT_SITE to CURRENT_SITE))
    }

    fun sendEditProductImpression(userId: String, action: String, label: String = "") {
        val map = mapOf(
                KEY_EVENT to EVENT_VIEW_EDIT_PRODUCT,
                KEY_CATEGORY to CAT_EDIT_PRODUCT_PAGE,
                KEY_ACTION to action,
                KEY_LABEL to label,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_USER_ID to userId
        )
        ProductVariantTracking.getTracker().sendGeneralEvent(map)
    }

    fun sendEditProductUpload(category: String, userId: String, label: String = "") {
        val map = mapOf(
                KEY_EVENT to EVENT_SERVER_ERROR,
                KEY_CATEGORY to category,
                KEY_ACTION to "",
                KEY_LABEL to label,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_USER_ID to userId
        )
        ProductVariantTracking.getTracker().sendGeneralEvent(map)
    }

    fun sendEditProductPgIrisViewEvent(action: String, label: String, trackerId: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_VIEW_PG_IRIS,
                KEY_ACTION to action,
                KEY_CATEGORY to CAT_EDIT_PRODUCT_PAGE,
                KEY_LABEL to label,
                KEY_TRACKER_ID to trackerId
        )
        getTracker().sendGeneralEvent(map)
    }

    fun sendEditProductPgIrisClickEvent(action: String, label: String = "", trackerId: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_CLICK_PG,
                KEY_ACTION to action,
                KEY_CATEGORY to CAT_EDIT_PRODUCT_PAGE,
                KEY_LABEL to label,
                KEY_TRACKER_ID to trackerId
        )
    }
}

object ProductVariantTracking {
    var gtmTracker: ContextAnalytics? = null
    const val KEY_SHOP_ID = "shopId"
    const val KEY_TRACKER_ID = "trackerId"
    const val KEY_SCREEN_NAME = "screenName"
    const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
    const val KEY_CURRENT_SITE = "currentSite"
    const val KEY_USER_ID = "userId"
    const val EVENT_CLICK_PG = "clickPG"
    const val EVENT_OPEN_SCREEN = "openScreen"
    const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    const val EVENT_CLICK_EDIT_PRODUCT = "clickEditProduct"
    const val EVENT_CLICK_MANAGE_PRODUCT = "clickManageProduct"
    const val CAT_ADD_VARIANT_PAGE = "add variant page"
    const val CAT_ADD_VARIANT_DETAIL_PAGE = "add detail variant page"
    const val CAT_EDIT_VARIANT_PAGE = "edit variant page"
    const val CAT_EDIT_VARIANT_DETAIL_PAGE = "edit detail variant page"
    const val CAT_ADD_VARIANT_MANAGE_PAGE = "product variant page - add"
    const val CAT_EDIT_VARIANT_MANAGE_PAGE = "product variant page - edit"
    const val CAT_VARIANT_MANAGE_PAGE = "product variant page"
    const val CURRENT_SITE_MARKRTPLACE = "tokopediamarketplace"

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

    fun sendAddProductWeightPerVariant(action: String, label: String, shopId: String, trackerId: String) {
        val map = mapOf(
            KEY_EVENT to EVENT_CLICK_PG,
            KEY_CATEGORY to CAT_VARIANT_MANAGE_PAGE,
            KEY_ACTION to action,
            KEY_LABEL to label,
            KEY_SHOP_ID to shopId,
            KEY_TRACKER_ID to trackerId,
            KEY_CURRENT_SITE to CURRENT_SITE_MARKRTPLACE,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT_CAPITALIZED_WORD
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

    fun sendCustomVariantTypeEventClick(eventAction: String, eventLabel: String, isEdit: Boolean, shopId: String) {
        val map = mapOf(
                KEY_EVENT to EVENT_CLICK_MANAGE_PRODUCT,
                KEY_ACTION to eventAction,
                KEY_CATEGORY to if (isEdit) CAT_EDIT_VARIANT_MANAGE_PAGE else CAT_ADD_VARIANT_MANAGE_PAGE,
                KEY_LABEL to eventLabel,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_SHOP_ID to shopId
        )
        getTracker().sendGeneralEvent(map)
    }
}

object ProductDraftTracking {
    private const val PAGE_SOURCE = "pageSource"
    private const val CLICK_DRAFT_PRODUCT = "clickDraftProduct"
    private const val CLICK = "Click"
    private const val EVENT_CLICK_ADD_PRODUCT = "clickAddProduct"
    private const val CAT_DRAFT_PRODUCT_PAGE = "draft product page"
    private const val KEY_SHOP_ID = "shopId"
    private const val DRAFT_PRODUCT = "Draft Product"
    const val CLICK_ADD_PRODUCT_WITHOUT_DRAFT = "click add product without draft"
    const val CLICK_ADD_PRODUCT = "click add product"
    const val EDIT_DRAFT = "Edit Draft"
    const val ADD_PRODUCT = "Add Product"
    const val DELETE_DRAFT = "Delete Draft"

    fun getTracker(): ContextAnalytics {
        if (ProductVariantTracking.gtmTracker == null) {
            ProductVariantTracking.gtmTracker = TrackApp.getInstance().getGTM()
        }
        return ProductVariantTracking.gtmTracker!!
    }

    fun sendProductDraftClick(label: String) {
        getTracker().sendGeneralEvent(
                CLICK_DRAFT_PRODUCT,
                DRAFT_PRODUCT,
                CLICK,
                label
        )
    }

    fun sendScreenProductDraft(screenName: String, pageSource: String) {
        val customDimensions = mapOf(
                PAGE_SOURCE to pageSource
        )
        getTracker().sendScreenAuthenticated(
                screenName,
                customDimensions
        )
    }

    fun sendAddProductClick(shopId: String, action: String) {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_ADD_PRODUCT,
                CAT_DRAFT_PRODUCT_PAGE,
                action,
                "",
                mapOf(KEY_SHOP_ID to shopId)
        )
    }
}

object ProductCategoryTracking {
    private const val ACTION_BACK_CHOOSE_OTHER = "click back choose other categories"
    var gtmTracker: ContextAnalytics? = null

    fun getTracker(): ContextAnalytics {
        if (gtmTracker == null) {
            gtmTracker = TrackApp.getInstance().gtm
        }
        return gtmTracker as ContextAnalytics
    }

    fun clickBackOtherCategory(shopId: String) {
        getTracker().sendGeneralEventCustom(
                EVENT_CLICK_EDIT_PRODUCT,
                CAT_EDIT_PRODUCT_PAGE,
                ACTION_BACK_CHOOSE_OTHER,
                "",
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