package com.tokopedia.ordermanagement.snapshot.analytics

import android.os.Bundle
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 2/1/21.
 */
object SnapshotAnalytics {
    // event keys
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_USER_ID = "userId"
    private const val KEY_PRODUCT_ID = "productId"
    private const val KEY_SHOP_ID = "shopId"
    private const val KEY_CUSTOM_DIMENSION_40 = "dimension40"
    private const val KEY_INDEX = "index"
    private const val KEY_PRICE = "price"
    private const val KEY_ITEMS = "items"
    private const val KEY_ITEM_BRAND = "item_brand"
    private const val KEY_ITEM_CATEGORY = "item_category"
    private const val KEY_ITEM_ID = "item_id"
    private const val KEY_ITEM_NAME = "item_name"
    private const val KEY_ITEM_VARIANT = "item_variant"

    // event names
    private const val EVENT_NAME_CLICK_BOM = "clickBOM"
    private const val EVENT_NAME_CLICK_SOM = "clickSOM"
    private const val EVENT_NAME_SELECT_CONTENT = "select_content"

    // event categories
    private const val EVENT_CATEGORY_BOM_PRODUCT_SNAPSHOT_PAGE = "bom - product snapshot page"
    private const val EVENT_CATEGORY_SOM_PRODUCT_SNAPSHOT_PAGE = "som - product snapshot page"
    private const val EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP = "my purchase list detail - mp"

    // event actions
    private const val EVENT_ACTION_CLICK_PRODUCT_PAGE = "click product page"
    private const val EVENT_ACTION_CLICK_SHOP_PAGE = "click shop page"
    private const val EVENT_ACTION_CLICK_SEE_PRODUCT_PAGE = "click lihat halaman product"

    // current sites
    private const val CURRENT_SITE_TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val CURRENT_SITE_MARKETPLACE = "marketplace"

    // business units
    private const val BUSINESS_UNIT_SOM = "Seller Order Management"
    private const val BUSINESS_UNIT_PHYSICAL_GOODS = "Physical Goods"

    // others
    private const val MARKER_ORDER_LIST_DETAIL_MARKETPLACE = "/order list detail - marketplace"

    private fun Bundle.appendGeneralEventData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ): Bundle {
        putString(TrackAppUtils.EVENT, eventName)
        putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
        putString(TrackAppUtils.EVENT_ACTION, eventAction)
        putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        return this
    }

    private fun Bundle.appendBusinessUnit(businessUnit: String): Bundle {
        putString(KEY_BUSINESS_UNIT, businessUnit)
        return this
    }

    private fun Bundle.appendCurrentSite(currentSite: String): Bundle {
        putString(KEY_CURRENT_SITE, currentSite)
        return this
    }

    private fun Bundle.appendProductId(productId: String): Bundle {
        putString(KEY_PRODUCT_ID, productId)
        return this
    }

    private fun Bundle.appendUserId(userId: String): Bundle {
        putString(KEY_USER_ID, userId)
        return this
    }

    private fun Bundle.sendEnhancedEcommerce(eventName: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, this)
    }

    private fun Bundle.appendBuyAgainProductEE(
        productId: String,
        productName: String,
        productCategory: String,
        productPrice: Double
    ): Bundle {
        val productsPayload = Bundle().apply {
            putString(KEY_CUSTOM_DIMENSION_40, MARKER_ORDER_LIST_DETAIL_MARKETPLACE)
            putInt(KEY_INDEX, 0)
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, productCategory)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, "")
            putDouble(KEY_PRICE, productPrice)
        }
        putParcelableArrayList(KEY_ITEMS, arrayListOf(productsPayload))
        return this
    }

    fun clickSeeProductPageFromBOM(
        orderId: String,
        productId: String,
        productName: String,
        productCategory: String,
        productPrice: Double,
        userId: String
    ) {
        Bundle().appendGeneralEventData(
            eventName = EVENT_NAME_SELECT_CONTENT,
            eventCategory = EVENT_CATEGORY_MY_PURCHASE_LIST_DETAIL_MP,
            eventAction = EVENT_ACTION_CLICK_SEE_PRODUCT_PAGE,
            eventLabel = orderId
        ).appendBusinessUnit(BUSINESS_UNIT_PHYSICAL_GOODS)
            .appendCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            .appendBuyAgainProductEE(productId, productName, productCategory, productPrice)
            .appendProductId(productId)
            .appendUserId(userId)
            .sendEnhancedEcommerce(EVENT_NAME_SELECT_CONTENT)
    }

    fun clickShopPage(shopId: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_BOM,
            EVENT_CATEGORY_BOM_PRODUCT_SNAPSHOT_PAGE,
            EVENT_ACTION_CLICK_SHOP_PAGE,
            ""
        )
        event[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        event[KEY_USER_ID] = userId
        event[KEY_SHOP_ID] = shopId
        event[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SOM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickSeeProductPageFromSOM(productId: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SOM,
            EVENT_CATEGORY_SOM_PRODUCT_SNAPSHOT_PAGE,
            EVENT_ACTION_CLICK_PRODUCT_PAGE,
            ""
        )
        event[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        event[KEY_USER_ID] = userId
        event[KEY_PRODUCT_ID] = productId
        event[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SOM
        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickShopPageFromSOM(shopId: String, userId: String) {
        val event = TrackAppUtils.gtmData(
            EVENT_NAME_CLICK_SOM,
            EVENT_CATEGORY_SOM_PRODUCT_SNAPSHOT_PAGE,
            EVENT_ACTION_CLICK_SHOP_PAGE,
            ""
        )
        event[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKETPLACE
        event[KEY_USER_ID] = userId
        event[KEY_SHOP_ID] = shopId
        event[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SOM
        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}