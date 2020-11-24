package com.tokopedia.sellerappwidget.common

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferencesImpl
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object AppWidgetHelper {

    fun getRemoteView(context: Context, @LayoutRes layoutRes: Int): RemoteViews {
        return RemoteViews(context.packageName, layoutRes)
    }

    fun getOrderWidgetRemoteView(context: Context): RemoteViews {
        return RemoteViews(context.packageName, R.layout.saw_app_widget_order)
    }

    fun getOrderWidgetLastUpdatedFmt(context: Context): String {
        val sharedPref: SellerAppWidgetPreferences = SellerAppWidgetPreferencesImpl(context)
        val lastUpdatedMillis = sharedPref.getLong(Const.SharedPrefKey.ORDER_LAST_UPDATED, System.currentTimeMillis())
        val now = Date()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateSdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val diff = lastUpdatedMillis - now.time
        val oneDayMillis = TimeUnit.DAYS.toMillis(1)

        val lastUpdatedHour = sdf.format(Date(lastUpdatedMillis))

        return when {
            diff <= oneDayMillis -> { //same day
                lastUpdatedHour
            }
            diff > oneDayMillis.times(2) -> { //more than 2 days
                dateSdf.format(lastUpdatedMillis) + " $lastUpdatedHour"
            }
            else -> { //yesterday
                context.getString(R.string.saw_yesterday) + " $lastUpdatedHour"
            }
        }
    }

    @WidgetSize
    fun getAppWidgetSize(widgetHeight: Int): String {
        var n = 2
        while (70 * n - 2 < widgetHeight) {
            ++n
        }
        val size = n - 1
        return when {
            size <= 3 -> WidgetSize.SMALL
            size > 5 -> WidgetSize.LARGE
            else -> WidgetSize.NORMAL
        }
    }

    inline fun <reified T : AppWidgetProvider> getAppWidgetIds(context: Context, awm: AppWidgetManager): IntArray {
        return awm.getAppWidgetIds(ComponentName(context, T::class.java))
    }
}