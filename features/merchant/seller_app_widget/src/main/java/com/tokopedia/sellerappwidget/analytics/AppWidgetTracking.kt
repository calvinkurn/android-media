package com.tokopedia.sellerappwidget.analytics

import android.content.Context
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferencesImpl
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

    private val userSession: UserSessionInterface by lazy {
        UserSession(context)
    }
    private val appWidgetPref: SellerAppWidgetPreferences by lazy {
        SellerAppWidgetPreferencesImpl(context)
    }

    fun sendEventImpressionSuccessStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_ACTIVE_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionEmptyStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_EMPTY_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionLoadingStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_LOADING_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionNoAccessStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_ACCESS_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionNoLoginStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_LOGIN_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionErrorStateChatWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_CONNECTION_STATE
        sendEventOnceADayChat(action) {
            val eventMap = createImpressionChatWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventClickSellerIconChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_SELLER_ICON)
        sendEvent(eventMap)
    }

    fun sendEventClickRefreshButtonChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_REFRESH_BUTTON)
        sendEvent(eventMap)
    }

    fun sendEventClickItemChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_CHAT_LINE)
        sendEvent(eventMap)
    }

    fun sendEventClickCheckNowChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_CHECK_NOW)
        sendEvent(eventMap)
    }

    fun sendEventClickLoginNowChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_LOGIN_NOW)
        sendEvent(eventMap)
    }

    fun sendEventClickShopNameChatWidget() {
        val eventMap = createClickChatWidget(TrackingConstant.Action.CLICK_SHOP_NAME_AND_CHAT)
        sendEvent(eventMap)
    }

    fun sendEventImpressionNewOrderOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_ACTIVE_NEW_ORDER
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionReadyShippingOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_ACTIVE_READY_SHIPPING
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionEmptyStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_EMPTY_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionLoadingStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_LOADING_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionNoAccessStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_ACCESS_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionNoConnectionStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_CONNECTION_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventImpressionNoLoginStateOrderWidget() {
        val action = TrackingConstant.Action.IMPRESSION_NO_LOGIN_STATE
        sendEventOnceADayOrder(action) {
            val eventMap = createImpressionOrderWidget(action)
            sendEvent(eventMap)
        }
    }

    fun sendEventClickSellerIconOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_SELLER_ICON)
        sendEvent(eventMap)
    }

    fun sendEventClickShopNameOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_SHOP_NAME_AND_ORDER)
        sendEvent(eventMap)
    }

    fun sendEventClickItemOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_ORDER_LINE)
        sendEvent(eventMap)
    }

    fun sendEventClickLoginNowOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_LOGIN_NOW)
        sendEvent(eventMap)
    }

    fun sendEventClickCheckNowOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_CHECK_NOW)
        sendEvent(eventMap)
    }

    fun sendEventClickRefreshButtonOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_REFRESH_BUTTON)
        sendEvent(eventMap)
    }

    fun sendEventClickSwitchButtonOrderWidget() {
        val eventMap = createClickOrderWidget(TrackingConstant.Action.CLICK_BUTTON_STATUS)
        sendEvent(eventMap)
    }

    private fun createImpressionOrderWidget(action: String): MutableMap<String, Any> {
        return createMap(
                TrackingConstant.Event.VIEW_ORDER_WIDGET,
                TrackingConstant.Category.ORDER_WIDGET,
                action,
                ""
        )
    }

    private fun createClickOrderWidget(action: String): MutableMap<String, Any> {
        return createMap(
                TrackingConstant.Event.CLICK_ORDER_WIDGET,
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

    private fun sendEvent(eventMap: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    /**
     * send event if last send is more than 24 hours
     * @param action is the event action.
     * */
    private fun sendEventOnceADayChat(action: String, callback: () -> Unit) {
        val actionKey = "chat_" + action.replace(" ", "")
        val nowMillis = System.currentTimeMillis()
        val last24hours = nowMillis.minus(TimeUnit.DAYS.toMillis(1))
        val lastSend = appWidgetPref.getLong(actionKey, nowMillis)

        if (lastSend > last24hours) {
            appWidgetPref.putLong(actionKey, nowMillis)
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
        val last24hours = nowMillis.minus(TimeUnit.DAYS.toMillis(1))
        val lastSend = appWidgetPref.getLong(actionKey, nowMillis)

        if (lastSend > last24hours) {
            appWidgetPref.putLong(actionKey, nowMillis)
            callback()
        }
    }
}