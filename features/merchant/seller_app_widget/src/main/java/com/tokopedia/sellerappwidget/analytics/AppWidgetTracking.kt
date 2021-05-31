package com.tokopedia.sellerappwidget.analytics

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 11/12/20
 */

//data layer : https://mynakama.tokopedia.com/datatracker/requestdetail/view/151

class AppWidgetTracking(context: Context) {

    companion object {
        private var appWidgetTracking: AppWidgetTracking? = null

        fun getInstance(context: Context): AppWidgetTracking {
            if (appWidgetTracking == null) {
                appWidgetTracking = AppWidgetTracking(context)
            }
            return appWidgetTracking!!
        }
    }

    private val userSession: UserSessionInterface by lazy {
        UserSession(context)
    }
    private val cacheHandler: LocalCacheHandler by lazy {
        AppWidgetHelper.getCacheHandler(context)
    }

    fun sendEventImpressionSuccessStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_ACTIVE_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEventChatWidget(eventMap)
        }
    }

    fun sendEventImpressionEmptyStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_EMPTY_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEventChatWidget(eventMap)
        }
    }

    fun sendEventImpressionLoadingStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_LOADING_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEventChatWidget(eventMap)
        }
    }

    fun sendEventImpressionNoLoginStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_LOGIN_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEventChatWidget(eventMap)
        }
    }

    fun sendEventImpressionErrorStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_CONNECTION_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEventChatWidget(eventMap)
        }
    }

    fun sendEventClickSellerIconChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_SELLER_ICON)
        sendEventChatWidget(eventMap)
    }

    fun sendEventClickRefreshButtonChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_REFRESH_BUTTON)
        sendEventChatWidget(eventMap)
    }

    fun sendEventClickItemChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_CHAT_LINE)
        sendEventChatWidget(eventMap)
    }

    fun sendEventClickCheckNowChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_CHECK_NOW)
        sendEventChatWidget(eventMap)
    }

    fun sendEventClickLoginNowChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_LOGIN_NOW)
        sendEventChatWidget(eventMap)
    }

    fun sendEventClickShopNameChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_SHOP_NAME_AND_CHAT)
        sendEventChatWidget(eventMap)
    }

    fun sendEventImpressionNewOrderOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_ACTIVE_NEW_ORDER
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventImpressionReadyShippingOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_ACTIVE_READY_SHIPPING
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventImpressionSmallSuccessStateNewOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NEW_ORDER_SMALL_ORDER
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventImpressionEmptyStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_EMPTY_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventImpressionLoadingStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_LOADING_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventImpressionErrorStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_CONNECTION_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventImpressionNoLoginStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_LOGIN_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEventOrderWidget(eventMap)
        }
    }

    fun sendEventClickSmallNewOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_NEW_ORDER_SMALL_ORDER)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickSmallReadyToShipOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_READY_TO_SHIP_SMALL_ORDER)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickSellerIconOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_SELLER_ICON)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickShopNameOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_SHOP_NAME_AND_ORDER)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickItemOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_ORDER_LINE)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickLoginNowOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_LOGIN_NOW)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickCheckNowOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_CHECK_NOW)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickRefreshButtonOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_REFRESH_BUTTON)
        sendEventOrderWidget(eventMap)
    }

    fun sendEventClickSwitchButtonOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_BUTTON_STATUS)
        sendEventOrderWidget(eventMap)
    }

    private fun createImpressionOrderWidget(action: String): MutableMap<String, Any> {
        return createMap(
                TrackingConstant.Event.VIEW_CHAT_WIDGET,
                TrackingConstant.Category.ORDER_WIDGET,
                action,
                ""
        )
    }

    private fun createClickOrderWidget(action: String): MutableMap<String, Any> {
        return createMap(
                TrackingConstant.Event.CLICK_CHAT_WIDGET,
                TrackingConstant.Category.ORDER_WIDGET,
                action,
                ""
        )
    }

    private fun createImpressionChatWidget(action: String): MutableMap<String, Any> {
        return createMap(
                TrackingConstant.Event.VIEW_CHAT_WIDGET,
                TrackingConstant.Category.CHAT_WIDGET,
                action,
                ""
        )
    }

    private fun createClickChatWidget(action: String): MutableMap<String, Any> {
        return createMap(
                TrackingConstant.Event.CLICK_CHAT_WIDGET,
                TrackingConstant.Category.CHAT_WIDGET,
                action,
                ""
        )
    }

    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        val eventMap = TrackAppUtils.gtmData(event, category, action, label)
        eventMap[TrackingConstant.Key.KEY_BUSINESS_UNIT] = TrackingConstant.Value.PHYSICAL_GOODS
        eventMap[TrackingConstant.Key.KEY_CURRENT_SITE] = TrackingConstant.Value.TOKOPEDIASELLER
        eventMap[TrackingConstant.Key.KEY_USER_ID] = userSession.userId
        return eventMap
    }

    private fun sendEventChatWidget(eventMap: Map<String, Any>) {
        val isWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.CHAT_WIDGET_ENABLED, false)
        if (isWidgetEnabled) {
            TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
        }
    }

    private fun sendEventOrderWidget(eventMap: Map<String, Any>) {
        val isWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, false)
        if (isWidgetEnabled) {
            TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
        }
    }

    /**
     * send event if last send is more than 24 hours
     * @param action is the event action.
     * */
    private fun sendEventOnceADayChat(action: String, callback: () -> Unit) {
        val actionKey = "chat_" + action.replace(" ", "")
        val nowMillis = System.currentTimeMillis()
        val oneDayMillis = TimeUnit.DAYS.toMillis(1)
        val lastSend = cacheHandler.getLong(actionKey, nowMillis)
        val isWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.CHAT_WIDGET_ENABLED, false)
        if ((lastSend.plus(oneDayMillis) <= nowMillis || lastSend == nowMillis) && isWidgetEnabled) {
            cacheHandler.putLong(actionKey, nowMillis)
            cacheHandler.applyEditor()
            callback()
        }
    }

    /**
     * send event if last send is more than 24 hours
     * @param action is the event action.
     * */
    private fun sendEventOnceADayOrder(action: String, callback: () -> Unit) {
        val actionKey = "order_" + action.replace(" ", "")
        val nowMillis = System.currentTimeMillis()
        val oneDayMillis = TimeUnit.DAYS.toMillis(1)
        val lastSend = cacheHandler.getLong(actionKey, nowMillis)
        val isWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, false)
        if ((lastSend.plus(oneDayMillis) <= nowMillis || lastSend == nowMillis) && isWidgetEnabled) {
            cacheHandler.putLong(actionKey, nowMillis)
            cacheHandler.applyEditor()
            callback()
        }
    }
}