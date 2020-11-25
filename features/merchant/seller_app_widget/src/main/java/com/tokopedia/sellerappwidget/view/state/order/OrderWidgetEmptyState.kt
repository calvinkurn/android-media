package com.tokopedia.sellerappwidget.view.state.order

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.*

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetEmptyState {

    fun setupEmptyState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val awm = AppWidgetManager.getInstance(context)
        val option = awm.getAppWidgetOptions(widgetId)
        val minHeight = option.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        OrderWidgetStateHelper.updateViewOnEmpty(remoteViews)
        @WidgetSize val widgetSize = AppWidgetHelper.getAppWidgetSize(minHeight)
        when (widgetSize) {
            WidgetSize.SMALL -> showSmallWidgetEmptyState(context, remoteViews, widgetId)
            WidgetSize.NORMAL -> showNormalWidgetEmptyState(context, remoteViews, widgetId)
            else -> showLargeWidgetEmptyState(context, remoteViews, widgetId)
        }
    }

    private fun showSmallWidgetEmptyState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        with(remoteViews) {
            setInt(R.id.orderSawNormalEmptyState, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.orderSawSmallEmptyState, Const.Method.SET_VISIBILITY, View.VISIBLE)

            val title = context.getString(R.string.saw_order_small_empty_state_title)
            val description = context.getString(R.string.saw_order_small_empty_state_description)
            val cta = context.getString(R.string.saw_check_now)
            setTextViewText(R.id.tvSawSmallOrderCommonTitle, title)
            setTextViewText(R.id.tvSawSmallOrderCommonDescription, description)
            setTextViewText(R.id.tvSawSmallOrderCommonCta, cta)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawSmallOrderCommon, Const.Images.ORDER_ON_EMPTY, widgetId)

            val appLink = ApplinkConst.SellerApp.CENTRALIZED_PROMO
            registerAppLinkIntent(context, R.id.tvSawSmallOrderCommonCta, appLink, widgetId)
        }
    }

    private fun showNormalWidgetEmptyState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        with(remoteViews) {
            setInt(R.id.orderSawSmallEmptyState, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.orderSawNormalEmptyState, Const.Method.SET_VISIBILITY, View.VISIBLE)
        }
    }

    private fun showLargeWidgetEmptyState(context: Context, remoteViews: RemoteViews, widgetId: Int) {

    }
}