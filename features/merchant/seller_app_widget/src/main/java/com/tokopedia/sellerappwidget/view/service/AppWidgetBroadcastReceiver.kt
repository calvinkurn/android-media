package com.tokopedia.sellerappwidget.view.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.executor.GetChatExecutor
import com.tokopedia.sellerappwidget.view.executor.GetOrderExecutor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 08/12/20
 */

class AppWidgetBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_GET_CHAT_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.GET_CHAT_APP_WIDGET_DATA"
        private const val ACTION_GET_ORDER_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.GET_ORDER_APP_WIDGET_DATA"
        private const val ACTION_GET_ALL_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.GET_ALL_APP_WIDGET_DATA"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val userSession: UserSessionInterface = UserSession(context)
        if (!userSession.isLoggedIn) return
        val cacheHandler = AppWidgetHelper.getCacheHandler(context)

        when (intent.action) {
            ACTION_GET_CHAT_APP_WIDGET_DATA -> fetchChatWidgetData(context, cacheHandler)
            ACTION_GET_ORDER_APP_WIDGET_DATA -> fetchOrderWidgetData(context, cacheHandler)
            ACTION_GET_ALL_APP_WIDGET_DATA -> {
                fetchChatWidgetData(context, cacheHandler)
                fetchOrderWidgetData(context, cacheHandler)
            }
        }
    }

    private fun fetchChatWidgetData(context: Context, cacheHandler: LocalCacheHandler) {
        val isChatWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.CHAT_WIDGET_ENABLED, false)
        if (isChatWidgetEnabled) {
            GetChatExecutor.run(context)
        }
    }

    private fun fetchOrderWidgetData(context: Context, cacheHandler: LocalCacheHandler) {
        val isOrderWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, false)
        if (isOrderWidgetEnabled) {
            val orderStatusId: Int = cacheHandler.getInt(Const.SharedPrefKey.LAST_SELECTED_ORDER_TYPE, OrderAppWidget.DEFAULT_ORDER_STATUS_ID)
            GetOrderExecutor.run(context, orderStatusId)
        }
    }
}