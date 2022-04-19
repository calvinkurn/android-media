package com.tokopedia.sellerappwidget.view.state.chat

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.view.appwidget.ChatAppWidget
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 26/11/20
 */

object ChatWidgetErrorState {

    fun setupErrorState(context: Context, awm: AppWidgetManager, remoteViews: RemoteViews, widgetId: Int) {
        val option = awm.getAppWidgetOptions(widgetId)
        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)
        ChatWidgetStateHelper.updateViewCommonState(remoteViews, widgetSize)
        when (widgetSize) {
            WidgetSize.NORMAL -> showNormalWidgetErrorState(context, remoteViews, widgetId)
            else -> showLargeWidgetErrorState(context, remoteViews, widgetId)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventImpressionErrorStateChatWidget()
    }

    private fun showNormalWidgetErrorState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_order_small_error_state_title_normal),
                description = context.getString(R.string.saw_order_small_error_state_description_normal),
                imgUrl = Const.Images.COMMON_ON_ERROR
        )
        ChatWidgetStateHelper.setupNormalCommonWidget<ChatAppWidget>(context, remoteViews, data)
    }

    private fun showLargeWidgetErrorState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_order_small_error_state_title_normal),
                description = context.getString(R.string.saw_order_small_error_state_description_normal),
                imgUrl = Const.Images.COMMON_ON_ERROR
        )
        ChatWidgetStateHelper.setupLargeCommonWidget<ChatAppWidget>(context, remoteViews, data)
    }
}