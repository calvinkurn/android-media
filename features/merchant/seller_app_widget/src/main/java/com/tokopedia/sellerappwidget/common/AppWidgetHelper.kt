package com.tokopedia.sellerappwidget.common

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.widget.RemoteViews
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
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

        //n = 70 × n − 30
        val widgetSize = (ceil(minHeight + 30.0) / 70.0).toInt()
        return when {
            widgetSize <= 3 -> WidgetSize.SMALL
            widgetSize > 5 -> WidgetSize.LARGE
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

    fun getCacheHandler(context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, Const.SHARED_PREF_NAME)
    }

    fun setOrderAppWidgetEnabled(context: Context, isEnabled: Boolean) {
        val cacheHandler = getCacheHandler(context)
        cacheHandler.putBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, isEnabled)
        cacheHandler.applyEditor()
    }

    fun setChatAppWidgetEnabled(context: Context, isEnabled: Boolean) {
        val cacheHandler = getCacheHandler(context)
        cacheHandler.putBoolean(Const.SharedPrefKey.CHAT_WIDGET_ENABLED, isEnabled)
        cacheHandler.applyEditor()
    }

    fun isScreenOn(context: Context): Boolean {
        val pm = context.getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return false
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            pm.isInteractive
        } else {
            pm.isScreenOn
        }
    }
}