package com.tokopedia.sellerappwidget.view.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget

/**
 * Created By @ilhamsuaib on 08/12/20
 */

class AppWidgetBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_GET_CHAT_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.view.service.AppWidgetIntentService.GET_CHAT_APP_WIDGET_DATA"
        private const val ACTION_GET_ORDER_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.view.service.AppWidgetIntentService.GET_ORDER_APP_WIDGET_DATA"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_GET_CHAT_APP_WIDGET_DATA -> GetChatService.startService(context)
            ACTION_GET_ORDER_APP_WIDGET_DATA -> GetOrderService.startService(context, OrderAppWidget.DEFAULT_ORDER_STATUS_ID)
        }
    }
}