package com.tokopedia.sellerappwidget.view.state.order

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.*
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetStateHelper {

    private val viewIds = listOf(
            R.id.containerSawSmallCommonState,
            R.id.containerSawNormalCommonState,
            R.id.containerSawLargeCommonState,
            R.id.containerSawOrderListLoading,
            R.id.containerSawOrderListSuccess
    )

    fun setupSmallCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        with(remoteViews) {
            setTextViewText(R.id.tvSawSmallOrderCommonTitle, data.title)
            setTextViewText(R.id.tvSawSmallOrderCommonDescription, data.description)
            setTextViewText(R.id.tvSawSmallOrderCommonCta, data.ctaText)

            val ctaVisibility = if (data.appLink.isNotBlank() && data.ctaText.isNotBlank()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setInt(R.id.tvSawSmallOrderCommonCta, Const.Method.SET_VISIBILITY, ctaVisibility)
            setInt(R.id.btnSawSmallOrderRefreshCommon, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)

            setupRefreshIntent(context, this, R.id.btnSawSmallOrderRefreshCommon, data.widgetId)
            registerAppLinkIntent(context, R.id.tvSawSmallOrderCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawSmallOrderCommon, data.imgUrl, data.widgetId)
        }
    }

    fun setupNormalCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        with(remoteViews) {
            setTextViewText(R.id.tvSawNormalOrderCommonTitle, data.title)
            setTextViewText(R.id.tvSawNormalOrderCommonDescription, data.description)
            setTextViewText(R.id.tvSawNormalOrderCommonCta, data.ctaText)

            val ctaVisibility = if (data.appLink.isNotBlank() && data.ctaText.isNotBlank()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setInt(R.id.tvSawNormalOrderCommonCta, Const.Method.SET_VISIBILITY, ctaVisibility)
            setInt(R.id.btnSawNormalOrderRefreshCommon, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)

            setupRefreshIntent(context, this, R.id.btnSawNormalOrderRefreshCommon, data.widgetId)
            registerAppLinkIntent(context, R.id.tvSawNormalOrderCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawNormalOrderCommon, Const.Images.ORDER_ON_EMPTY, data.widgetId)
        }
    }

    fun setupLargeCommonWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        with(remoteViews) {
            setTextViewText(R.id.tvSawLargeOrderCommonTitle, data.title)
            setTextViewText(R.id.tvSawLargeOrderCommonDescription, data.description)
            setTextViewText(R.id.tvSawLargeOrderCommonCta, data.ctaText)

            val ctaVisibility = if (data.appLink.isNotBlank() && data.ctaText.isNotBlank()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setInt(R.id.tvSawLargeOrderCommonCta, Const.Method.SET_VISIBILITY, ctaVisibility)
            setInt(R.id.btnSawLargeOrderRefreshCommon, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)

            setupRefreshIntent(context, this, R.id.btnSawLargeOrderRefreshCommon, data.widgetId)
            registerAppLinkIntent(context, R.id.tvSawLargeOrderCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawLargeOrderCommon, data.imgUrl, data.widgetId)
        }
    }

    fun setLastUpdated(context: Context, remoteView: RemoteViews) {
        val updatedFmt = AppWidgetHelper.getOrderWidgetLastUpdatedFmt(context)
        val updated = context.getString(R.string.saw_updated)
        remoteView.setTextViewText(R.id.tvSawOrderUpdated, "$updated $updatedFmt")
        remoteView.setTextViewText(R.id.tvSawSmallOrderUpdated, "$updated $updatedFmt")
    }

    fun updateViewCommonState(remoteViews: RemoteViews, @WidgetSize widgetSize: String) {
        when(widgetSize) {
            WidgetSize.SMALL -> remoteViews.setOrderUiVisibility(R.id.containerSawSmallCommonState)
            WidgetSize.NORMAL -> remoteViews.setOrderUiVisibility(R.id.containerSawNormalCommonState)
            else -> remoteViews.setOrderUiVisibility(R.id.containerSawLargeCommonState)
        }
    }

    fun updateViewOnSuccess(remoteViews: RemoteViews) {
        remoteViews.setOrderUiVisibility(R.id.containerSawOrderListSuccess)
    }

    fun updateViewOnLoading(remoteViews: RemoteViews) {
        remoteViews.setOrderUiVisibility(R.id.containerSawOrderListLoading)
    }

    fun setupRefreshIntent(context: Context, remoteViews: RemoteViews, viewId: Int, widgetId: Int) {
        with(remoteViews) {
            //setup refresh button click event
            val reloadIntent = Intent(context, OrderAppWidget::class.java).apply {
                action = Const.Action.REFRESH
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val reloadPendingIntent = PendingIntent.getBroadcast(context, 0, reloadIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setOnClickPendingIntent(viewId, reloadPendingIntent)
        }
    }

    private fun RemoteViews.setOrderUiVisibility(viewToVisible: Int) {
        viewIds.forEach {
            val viewVisibility = if (it == viewToVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
            setInt(it, Const.Method.SET_VISIBILITY, viewVisibility)
        }
    }
}