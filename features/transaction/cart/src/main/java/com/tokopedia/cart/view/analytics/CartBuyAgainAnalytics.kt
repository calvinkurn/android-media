package com.tokopedia.cart.view.analytics

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics
import com.tokopedia.track.builder.Tracker

// Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4520
object CartBuyAgainAnalytics : TransactionAnalytics() {

    private const val VIEW_BUY_AGAIN_WIDGET_ON_CART_ACTION = "view buy again widget on cart"
    private const val IMPRESSION_PRODUCT_ON_BUY_AGAIN_WIDGET = "impression product on buy again widget"
    private const val CLICK_PRODUCT_ON_BUY_AGAIN_WIDGET = "click product on buy again widget"
    private const val CLICK_BELI_LAGI_BUTTON_ON_BUY_AGAIN_WIDGET = "click beli lagi button on buy again widget"
    private const val CLICK_LIHAT_SEMUA_ARROW_BUTTON_ON_BUY_AGAIN_WIDGET = "click lihat semua arrow button on buy again widget"
    private const val CLICK_LIHAT_SEMUA_BUTTON_ON_BUY_AGAIN_WIDGET = "click lihat semua button on buy again widget"
    private const val IMPRESSION_FLOATING_BUTTON = "impression floating button"
    private const val CLICK_FLOATING_BUTTON = "click floating button"

    private const val VIEW_BUY_AGAIN_WIDGET_ON_CART_ACTION_TRACKER_ID = "50161"
    private const val IMPRESSION_PRODUCT_ON_BUY_AGAIN_WIDGET_TRACKER_ID = "50162"
    private const val CLICK_PRODUCT_ON_BUY_AGAIN_WIDGET_TRACKER_ID = "50163"
    private const val CLICK_BELI_LAGI_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID = "50164"
    private const val CLICK_LIHAT_SEMUA_ARROW_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID = "50165"
    private const val CLICK_LIHAT_SEMUA_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID = "50166"
    private const val IMPRESSION_FLOATING_BUTTON_TRACKER_ID = "50167"
    private const val CLICK_FLOATING_BUTTON_TRACKER_ID = "50168"

    private const val ITEM_LIST_FORMAT = "/order list - rekomendasi untuk anda -"
    private const val ITEM_LIST_KEY = "item_list"

    fun sendViewBuyAgainWidgetOnCartEvent() {
        Tracker.Builder()
            .setEvent(ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS)
            .setEventAction(VIEW_BUY_AGAIN_WIDGET_ON_CART_ACTION)
            .setEventCategory(ConstantTransactionAnalytics.EventCategory.CART)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(ConstantTransactionAnalytics.ExtraKey.TRACKER_ID, VIEW_BUY_AGAIN_WIDGET_ON_CART_ACTION_TRACKER_ID)
            .setBusinessUnit(ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE)
            .setCurrentSite(ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)
            .build()
            .send()
    }

    fun sendImpressionProductOnBuyAgainWidgetEvent(itemList: String, items: List<Map<String, Any>>, userId: String) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_ITEM_LIST,
            ConstantTransactionAnalytics.EventCategory.CART,
            IMPRESSION_PRODUCT_ON_BUY_AGAIN_WIDGET,
            String.EMPTY
        )
        dataLayer[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = IMPRESSION_PRODUCT_ON_BUY_AGAIN_WIDGET_TRACKER_ID
        dataLayer[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        dataLayer[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        dataLayer[ITEM_LIST_KEY] = "$ITEM_LIST_FORMAT $itemList"
        dataLayer[ConstantTransactionAnalytics.ExtraKey.ITEMS] = items
        dataLayer[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userId
        sendEnhancedEcommerce(dataLayer)
    }

    fun sendClickProductOnBuyAgainWidgetEvent(itemList: String, items: List<Map<String, Any>>, userId: String) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.SELECT_CONTENT,
            ConstantTransactionAnalytics.EventCategory.CART,
            CLICK_PRODUCT_ON_BUY_AGAIN_WIDGET,
            String.EMPTY
        )
        dataLayer[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = CLICK_PRODUCT_ON_BUY_AGAIN_WIDGET_TRACKER_ID
        dataLayer[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        dataLayer[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        dataLayer[ITEM_LIST_KEY] = "$ITEM_LIST_FORMAT $itemList"
        dataLayer[ConstantTransactionAnalytics.ExtraKey.ITEMS] = items
        dataLayer[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userId
        sendEnhancedEcommerce(dataLayer)
    }

    fun sendClickBeliLagiButtonOnBuyAgainWidgetEvent(items: List<Map<String, Any>>, userId: String) {
        val dataLayer = getGtmData(
            ConstantTransactionAnalytics.EventName.ADD_TO_CART,
            ConstantTransactionAnalytics.EventCategory.CART,
            CLICK_BELI_LAGI_BUTTON_ON_BUY_AGAIN_WIDGET,
            String.EMPTY
        )
        dataLayer[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = CLICK_BELI_LAGI_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID
        dataLayer[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        dataLayer[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        dataLayer[ConstantTransactionAnalytics.ExtraKey.ITEMS] = items
        dataLayer[ConstantTransactionAnalytics.ExtraKey.USER_ID] = userId
        sendEnhancedEcommerce(dataLayer)
    }

    fun sendClickLihatSemuaArrowButtonOnBuyAgainWidgetEvent() {
        Tracker.Builder()
            .setEvent(ConstantTransactionAnalytics.EventName.CLICK_PP)
            .setEventAction(CLICK_LIHAT_SEMUA_ARROW_BUTTON_ON_BUY_AGAIN_WIDGET)
            .setEventCategory(ConstantTransactionAnalytics.EventCategory.CART)
            .setEventLabel("")
            .setCustomProperty(ConstantTransactionAnalytics.ExtraKey.TRACKER_ID, CLICK_LIHAT_SEMUA_ARROW_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID)
            .setBusinessUnit(ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE)
            .setCurrentSite(ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)
            .build()
            .send()
    }

    fun sendClickLihatSemuaButtonOnBuyAgainWidgetEvent() {
        Tracker.Builder()
            .setEvent(ConstantTransactionAnalytics.EventName.CLICK_PP)
            .setEventAction(CLICK_LIHAT_SEMUA_BUTTON_ON_BUY_AGAIN_WIDGET)
            .setEventCategory(ConstantTransactionAnalytics.EventCategory.CART)
            .setEventLabel("")
            .setCustomProperty(ConstantTransactionAnalytics.ExtraKey.TRACKER_ID, CLICK_LIHAT_SEMUA_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID)
            .setBusinessUnit(ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE)
            .setCurrentSite(ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)
            .build()
            .send()
    }

    fun sendImpressionFloatingButtonEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS)
            .setEventAction(IMPRESSION_FLOATING_BUTTON)
            .setEventCategory(ConstantTransactionAnalytics.EventCategory.CART)
            .setEventLabel(eventLabel)
            .setCustomProperty(ConstantTransactionAnalytics.ExtraKey.TRACKER_ID, IMPRESSION_FLOATING_BUTTON_TRACKER_ID)
            .setBusinessUnit(ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM)
            .setCurrentSite(ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)
            .build()
            .send()
    }

    fun sendClickFloatingButtonEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(ConstantTransactionAnalytics.EventName.CLICK_PP)
            .setEventAction(CLICK_FLOATING_BUTTON)
            .setEventCategory(ConstantTransactionAnalytics.EventCategory.CART)
            .setEventLabel(eventLabel)
            .setCustomProperty(ConstantTransactionAnalytics.ExtraKey.TRACKER_ID, CLICK_FLOATING_BUTTON_TRACKER_ID)
            .setBusinessUnit(ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM)
            .setCurrentSite(ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE)
            .build()
            .send()
    }
}
