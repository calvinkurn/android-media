package com.tokopedia.sellerappwidget.view.state.order

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 26/11/20
 */

object OrderWidgetErrorState {

    fun setupErrorState(context: Context, awm: AppWidgetManager, remoteViews: RemoteViews, widgetId: Int) {
        val option = awm.getAppWidgetOptions(widgetId)
        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)
        OrderWidgetStateHelper.updateViewCommonState(remoteViews, widgetSize)
        when (widgetSize) {
            WidgetSize.SMALL -> showSmallWidgetErrorState(context, remoteViews, widgetId)
            WidgetSize.NORMAL -> showNormalWidgetErrorState(context, remoteViews, widgetId)
            else -> showLargeWidgetErrorState(context, remoteViews, widgetId)
        }
    }

    private fun showSmallWidgetErrorState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_order_small_error_state_title_small),
                description = context.getString(R.string.saw_order_small_error_state_description_small),
                imgUrl = Const.Images.ORDER_ON_EMPTY
        )
        OrderWidgetStateHelper.setupSmallCommonWidget(context, remoteViews, data)
    }

    private fun showNormalWidgetErrorState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_order_small_error_state_title_normal),
                description = context.getString(R.string.saw_order_small_error_state_description_normal),
                imgUrl = Const.Images.ORDER_ON_EMPTY
        )
        OrderWidgetStateHelper.setupNormalCommonWidget(context, remoteViews, data)
    }

    private fun showLargeWidgetErrorState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_order_small_error_state_title_normal),
                description = context.getString(R.string.saw_order_small_error_state_description_normal),
                imgUrl = Const.Images.ORDER_ON_EMPTY
        )
        OrderWidgetStateHelper.setupLargeCommonWidget(context, remoteViews, data)
    }
}