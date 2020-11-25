package com.tokopedia.sellerappwidget.common

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget

/**
 * Created By @ilhamsuaib on 25/11/20
 */

fun RemoteViews.registerAppLinkIntent(context: Context, viewId: Int, appLink: String, widgetId: Int) {
    val appLinkIntent = Intent(context, OrderAppWidget::class.java).apply {
        action = Const.Action.OPEN_APPLINK
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        putExtra(Const.Extra.APP_LINK, appLink)
    }
    val appLinkPendingIntent = PendingIntent.getBroadcast(context, 0, appLinkIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    setOnClickPendingIntent(viewId, appLinkPendingIntent)
}