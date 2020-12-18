package com.tokopedia.sellerappwidget.common

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerappwidget.R
import kotlin.math.ceil

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object AppWidgetHelper {

    fun getOrderWidgetRemoteView(context: Context): RemoteViews {
        return RemoteViews(context.packageName, R.layout.saw_app_widget_order)
    }

    fun getChatWidgetRemoteView(context: Context): RemoteViews {
        return RemoteViews(context.packageName, R.layout.saw_app_widget_chat)
    }

    @WidgetSize
    fun getAppWidgetSize(option: Bundle): String {
        val minHeight = option.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        var n = 2
        while (70 * n - 2 < minHeight) {
            ++n
        }
        val size = (ceil(minHeight + 30.0) / 70.0).toInt()
        return when {
            size <= 3 -> WidgetSize.SMALL
            size > 5 -> WidgetSize.LARGE
            else -> WidgetSize.NORMAL
        }
    }

    fun getAppWidgetHeight(context: Context, widgetId: Int): Int {
        val awm = AppWidgetManager.getInstance(context)
        val option = awm.getAppWidgetOptions(widgetId)
        return option.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
    }

    inline fun <reified T : AppWidgetProvider> getAppWidgetIds(context: Context, awm: AppWidgetManager): IntArray {
        return awm.getAppWidgetIds(ComponentName(context, T::class.java))
    }

    fun openAppLink(context: Context, intent: Intent) {
        val appLink = intent.data?.toString().orEmpty()
        val appLinkIntent = RouteManager.getIntent(context, appLink).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(appLinkIntent)
    }
}