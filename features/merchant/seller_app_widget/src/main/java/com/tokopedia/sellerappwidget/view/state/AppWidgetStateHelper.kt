package com.tokopedia.sellerappwidget.view.state

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.common.registerAppLinkIntent
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 01/12/20
 */

abstract class AppWidgetStateHelper {

    internal inline fun <reified T : AppWidgetProvider> setupNormalCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        with(remoteViews) {
            setTextViewText(R.id.tvSawNormalCommonTitle, data.title)
            setTextViewText(R.id.tvSawNormalCommonDescription, data.description)
            setTextViewText(R.id.tvSawNormalCommonCta, data.ctaText)

            val ctaVisibility = if (data.appLink.isNotBlank() && data.ctaText.isNotBlank()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setInt(R.id.tvSawNormalCommonCta, Const.Method.SET_VISIBILITY, ctaVisibility)
            setInt(R.id.btnSawNormalRefreshCommon, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)
            setInt(R.id.btnSawNormalRefreshCommon, Const.Method.SET_VISIBILITY, View.VISIBLE)

            setupRefreshIntent<T>(context, this, R.id.btnSawNormalRefreshCommon, data.widgetId)
            registerAppLinkIntent<T>(context, R.id.tvSawNormalCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawNormalCommon, data.imgUrl, data.widgetId)
        }
    }

    internal inline fun <reified T : AppWidgetProvider> setupLargeCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        with(remoteViews) {
            setTextViewText(R.id.tvSawLargeCommonTitle, data.title)
            setTextViewText(R.id.tvSawLargeCommonDescription, data.description)
            setTextViewText(R.id.tvSawLargeCommonCta, data.ctaText)

            val ctaVisibility = if (data.appLink.isNotBlank() && data.ctaText.isNotBlank()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setInt(R.id.tvSawLargeCommonCta, Const.Method.SET_VISIBILITY, ctaVisibility)
            setInt(R.id.btnSawLargeRefreshCommon, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)
            setInt(R.id.btnSawLargeRefreshCommon, Const.Method.SET_VISIBILITY, View.VISIBLE)

            setupRefreshIntent<T>(context, this, R.id.btnSawLargeRefreshCommon, data.widgetId)
            registerAppLinkIntent<T>(context, R.id.tvSawLargeCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawLargeCommon, data.imgUrl, data.widgetId)
        }
    }

    inline fun <reified T : AppWidgetProvider> setupRefreshIntent(context: Context, remoteViews: RemoteViews, viewId: Int, widgetId: Int) {
        with(remoteViews) {
            //setup refresh button click event
            val reloadIntent = Intent(context, T::class.java).apply {
                action = Const.Action.REFRESH
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val reloadPendingIntent = PendingIntent.getBroadcast(context, 0, reloadIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setOnClickPendingIntent(viewId, reloadPendingIntent)
        }
    }

    protected fun getWidgetLastUpdatedFmt(context: Context, prefKey: String): String {
        val cacheHandler = AppWidgetHelper.getCacheHandler(context)
        val lastUpdatedMillis = cacheHandler.getLong(prefKey, System.currentTimeMillis())
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
}