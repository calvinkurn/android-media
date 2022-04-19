package com.tokopedia.sellerappwidget.view.state.order

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.common.registerAppLinkIntent
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel
import com.tokopedia.sellerappwidget.view.state.AppWidgetStateHelper

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetStateHelper : AppWidgetStateHelper() {

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
            setInt(R.id.btnSawSmallOrderRefreshCommon, Const.Method.SET_VISIBILITY, View.VISIBLE)
            setInt(R.id.btnSawNormalRefreshCommon, Const.Method.SET_VISIBILITY, View.VISIBLE)

            setupRefreshIntent<OrderAppWidget>(context, this, R.id.btnSawSmallOrderRefreshCommon, data.widgetId)
            registerAppLinkIntent<OrderAppWidget>(context, R.id.tvSawSmallOrderCommonCta, data.appLink, data.widgetId)

            Utils.loadImageIntoAppWidget(context, this, R.id.imgSawSmallOrderCommon, data.imgUrl, data.widgetId)
        }
    }

    fun setLastUpdated(context: Context, remoteView: RemoteViews) {
        val updatedFmt = getWidgetLastUpdatedFmt(context, Const.SharedPrefKey.ORDER_LAST_UPDATED)
        val updated = context.getString(R.string.saw_updated)
        remoteView.setTextViewText(R.id.tvSawOrderUpdated, "$updated $updatedFmt")
        remoteView.setTextViewText(R.id.tvSawSmallOrderUpdated, "$updated $updatedFmt")
    }

    fun updateViewCommonState(remoteViews: RemoteViews, @WidgetSize widgetSize: String) {
        when (widgetSize) {
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