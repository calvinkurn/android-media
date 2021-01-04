package com.tokopedia.sellerappwidget.view.state.chat

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize

/**
 * Created By @ilhamsuaib on 02/12/20
 */

object ChatWidgetLoadingState {

    private val loadingViews = listOf(
            R.id.shimmerSawLoadingItem5,
            R.id.shimmerSawLoadingItem6,
            R.id.shimmerSawLoadingItem7,
            R.id.shimmerSawLoadingItem8
    )

    fun setupLoadingState(context: Context, awm: AppWidgetManager, remoteView: RemoteViews, widgetId: Int) {
        val option = awm.getAppWidgetOptions(widgetId)
        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)
        when (widgetSize) {
            WidgetSize.NORMAL -> showNormalWidgetLoadingState(remoteView)
            else -> showLargeWidgetLoadingState(remoteView)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventImpressionLoadingStateChatWidget()
    }

    private fun showNormalWidgetLoadingState(remoteView: RemoteViews) = with(remoteView) {
        loadingViews.forEach {
            setInt(it, Const.Method.SET_VISIBILITY, View.GONE)
        }
    }

    private fun showLargeWidgetLoadingState(remoteView: RemoteViews) = with(remoteView) {
        loadingViews.forEach {
            setInt(it, Const.Method.SET_VISIBILITY, View.VISIBLE)
        }
    }
}