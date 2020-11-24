package com.tokopedia.sellerappwidget.view.state.order

import android.appwidget.AppWidgetManager
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize

/**
 * Created By @ilhamsuaib on 24/11/20
 */

object OrderWidgetLoadingState {

    fun setupLoadingState(awm: AppWidgetManager, remoteView: RemoteViews, widgetId: Int) {
        val option = awm.getAppWidgetOptions(widgetId)
        val minHeight = option.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        @WidgetSize val widgetSize = AppWidgetHelper.getAppWidgetSize(minHeight)
        println("AppWidget : widgetSize -> $widgetSize")
        when (widgetSize) {
            WidgetSize.SMALL -> showSmallWidgetLoadingState(remoteView)
            WidgetSize.NORMAL -> showNormalWidgetLoadingState(remoteView)
            else -> showLargeWidgetLoadingState(remoteView)
        }
    }

    private fun showSmallWidgetLoadingState(remoteView: RemoteViews) = with(remoteView) {
        setInt(R.id.shimmerSawLoadingItem4, Const.Method.SET_VISIBILITY, View.GONE)
        setInt(R.id.orderLoadingStateNormal, Const.Method.SET_VISIBILITY, View.GONE)
        setInt(R.id.orderLoadingStateSmall, Const.Method.SET_VISIBILITY, View.VISIBLE)
    }

    private fun showNormalWidgetLoadingState(remoteView: RemoteViews) = with(remoteView) {
        setInt(R.id.shimmerSawLoadingItem4, Const.Method.SET_VISIBILITY, View.GONE)
        setInt(R.id.orderLoadingStateSmall, Const.Method.SET_VISIBILITY, View.GONE)
        setInt(R.id.orderLoadingStateNormal, Const.Method.SET_VISIBILITY, View.VISIBLE)
    }

    private fun showLargeWidgetLoadingState(remoteView: RemoteViews) = with(remoteView) {
        setInt(R.id.orderLoadingStateSmall, Const.Method.SET_VISIBILITY, View.GONE)
        setInt(R.id.orderLoadingStateNormal, Const.Method.SET_VISIBILITY, View.VISIBLE)
        setInt(R.id.shimmerSawLoadingItem4, Const.Method.SET_VISIBILITY, View.VISIBLE)
    }
}