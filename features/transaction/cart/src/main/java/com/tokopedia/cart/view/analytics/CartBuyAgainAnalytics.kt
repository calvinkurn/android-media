package com.tokopedia.cart.view.analytics

import androidx.core.os.bundleOf
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.TransactionAnalytics

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
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            VIEW_BUY_AGAIN_WIDGET_ON_CART_ACTION,
            String.EMPTY
        )
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = VIEW_BUY_AGAIN_WIDGET_ON_CART_ACTION_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun sendImpressionProductOnBuyAgainWidgetEvent(itemList: String, items: List<Map<String, Any>>, userId: String) {
        val bundle = bundleOf(
            ConstantTransactionAnalytics.ExtraKey.EVENT to ConstantTransactionAnalytics.EventName.VIEW_ITEM_LIST,
            ConstantTransactionAnalytics.ExtraKey.EVENT_CATEGORY to ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.ExtraKey.EVENT_ACTION to IMPRESSION_PRODUCT_ON_BUY_AGAIN_WIDGET,
            ConstantTransactionAnalytics.ExtraKey.EVENT_LABEL to String.EMPTY,
            ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE to ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE,
            ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT to ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE,
            ConstantTransactionAnalytics.ExtraKey.TRACKER_ID to IMPRESSION_PRODUCT_ON_BUY_AGAIN_WIDGET_TRACKER_ID,
            ConstantTransactionAnalytics.ExtraKey.ITEMS to items,
            ITEM_LIST_KEY to "$ITEM_LIST_FORMAT $itemList",
            ConstantTransactionAnalytics.ExtraKey.USER_ID to userId
        )
        sendEnhancedEcommerce(ConstantTransactionAnalytics.EventName.VIEW_ITEM_LIST, bundle)
    }

    fun sendClickProductOnBuyAgainWidgetEvent(itemList: String, items: List<Map<String, Any>>, userId: String) {
        val bundle = bundleOf(
            ConstantTransactionAnalytics.ExtraKey.EVENT to ConstantTransactionAnalytics.EventName.SELECT_CONTENT,
            ConstantTransactionAnalytics.ExtraKey.EVENT_CATEGORY to ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.ExtraKey.EVENT_ACTION to CLICK_PRODUCT_ON_BUY_AGAIN_WIDGET,
            ConstantTransactionAnalytics.ExtraKey.EVENT_LABEL to String.EMPTY,
            ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE to ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE,
            ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT to ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE,
            ConstantTransactionAnalytics.ExtraKey.TRACKER_ID to CLICK_PRODUCT_ON_BUY_AGAIN_WIDGET_TRACKER_ID,
            ConstantTransactionAnalytics.ExtraKey.ITEMS to items,
            ITEM_LIST_KEY to "$ITEM_LIST_FORMAT $itemList",
            ConstantTransactionAnalytics.ExtraKey.USER_ID to userId
        )
        sendEnhancedEcommerce(ConstantTransactionAnalytics.EventName.SELECT_CONTENT, bundle)
    }

    fun sendClickBeliLagiButtonOnBuyAgainWidgetEvent(items: List<Map<String, Any>>, userId: String) {
        val bundle = bundleOf(
            ConstantTransactionAnalytics.ExtraKey.EVENT to ConstantTransactionAnalytics.EventName.ADD_TO_CART,
            ConstantTransactionAnalytics.ExtraKey.EVENT_CATEGORY to ConstantTransactionAnalytics.EventCategory.CART,
            ConstantTransactionAnalytics.ExtraKey.EVENT_ACTION to CLICK_BELI_LAGI_BUTTON_ON_BUY_AGAIN_WIDGET,
            ConstantTransactionAnalytics.ExtraKey.EVENT_LABEL to String.EMPTY,
            ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE to ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE,
            ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT to ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE,
            ConstantTransactionAnalytics.ExtraKey.TRACKER_ID to CLICK_BELI_LAGI_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID,
            ConstantTransactionAnalytics.ExtraKey.ITEMS to items,
            ConstantTransactionAnalytics.ExtraKey.USER_ID to userId
        )
        sendEnhancedEcommerce(ConstantTransactionAnalytics.EventName.ADD_TO_CART, bundle)
    }

    fun sendClickLihatSemuaArrowButtonOnBuyAgainWidgetEvent() {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            CLICK_LIHAT_SEMUA_ARROW_BUTTON_ON_BUY_AGAIN_WIDGET,
            String.EMPTY
        )
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = CLICK_LIHAT_SEMUA_ARROW_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun sendClickLihatSemuaButtonOnBuyAgainWidgetEvent() {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            CLICK_LIHAT_SEMUA_BUTTON_ON_BUY_AGAIN_WIDGET,
            String.EMPTY
        )
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = CLICK_LIHAT_SEMUA_BUTTON_ON_BUY_AGAIN_WIDGET_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_HOME_BROWSE
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun sendImpressionFloatingButtonEvent(eventLabel: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.VIEW_PP_IRIS,
            ConstantTransactionAnalytics.EventCategory.CART,
            IMPRESSION_FLOATING_BUTTON,
            eventLabel
        )
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = IMPRESSION_FLOATING_BUTTON_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }

    fun sendClickFloatingButtonEvent(eventLabel: String) {
        val gtmData = getGtmData(
            ConstantTransactionAnalytics.EventName.CLICK_PP,
            ConstantTransactionAnalytics.EventCategory.CART,
            CLICK_FLOATING_BUTTON,
            eventLabel
        )
        gtmData[ConstantTransactionAnalytics.ExtraKey.TRACKER_ID] = CLICK_FLOATING_BUTTON_TRACKER_ID
        gtmData[ConstantTransactionAnalytics.ExtraKey.BUSINESS_UNIT] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_BUSINESS_UNIT_PURCHASE_PLATFORM
        gtmData[ConstantTransactionAnalytics.ExtraKey.CURRENT_SITE] = ConstantTransactionAnalytics.CustomDimension.DIMENSION_CURRENT_SITE_MARKETPLACE
        sendGeneralEvent(gtmData)
    }
}
