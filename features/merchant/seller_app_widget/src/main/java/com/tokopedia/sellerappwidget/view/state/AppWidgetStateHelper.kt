package com.tokopedia.sellerappwidget.view.state

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.common.registerAppLinkIntent
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 01/12/20
 */

abstract class AppWidgetStateHelper {

    inline fun <reified T : AppWidgetProvider> setupNormalCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
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

            setupRefreshIntent<T>(context, this, R.id.btnSawNormalRefreshCommon, data.widgetId)
            registerAppLinkIntent(context, R.id.tvSawNormalCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawNormalCommon, Const.Images.ORDER_ON_EMPTY, data.widgetId)
        }
    }

    inline fun <reified T : AppWidgetProvider> setupLargeCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
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

            setupRefreshIntent<T>(context, this, R.id.btnSawLargeRefreshCommon, data.widgetId)
            registerAppLinkIntent(context, R.id.tvSawLargeCommonCta, data.appLink, data.widgetId)

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
}