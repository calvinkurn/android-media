package com.tokopedia.sellerappwidget.common

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews

/**
 * Created By @ilhamsuaib on 25/11/20
 */

internal inline fun <reified T : AppWidgetProvider> RemoteViews.registerAppLinkIntent(context: Context, viewId: Int, appLink: String, widgetId: Int, bundle: Bundle? = null) {
    val appLinkIntent = Intent(context, T::class.java).apply {
        action = Const.Action.OPEN_APPLINK
        data = Uri.parse(appLink)
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        bundle?.let {
            putExtra(Const.Extra.BUNDLE, it)
        }
    }
    val appLinkPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.getBroadcast(context, 0, appLinkIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getBroadcast(context, 0, appLinkIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    setOnClickPendingIntent(viewId, appLinkPendingIntent)
}
