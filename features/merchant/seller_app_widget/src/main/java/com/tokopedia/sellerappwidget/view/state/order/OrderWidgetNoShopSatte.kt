package com.tokopedia.sellerappwidget.view.state.order

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetNoShopState {

    fun setupEmptyState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val awm = AppWidgetManager.getInstance(context)
        val option = awm.getAppWidgetOptions(widgetId)
        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)

        OrderWidgetStateHelper.updateViewCommonState(remoteViews, widgetSize)

        val data = CommonStateUiModel(
            widgetId = widgetId,
            title = context.getString(R.string.saw_order_small_no_shop_state_title),
            description = context.getString(R.string.saw_order_small_no_shop_state_description),
            imgUrl = Const.Images.NO_SHOP_STATE,
            ctaText = context.getString(R.string.saw_create_shop),
            appLink = ApplinkConst.CREATE_SHOP
        )
        when (widgetSize) {
            WidgetSize.SMALL -> OrderWidgetStateHelper.setupSmallCommonWidget(
                context,
                remoteViews,
                data
            )
            WidgetSize.NORMAL -> OrderWidgetStateHelper.setupNormalCommonWidget<OrderAppWidget>(
                context,
                remoteViews,
                data
            )
            else -> OrderWidgetStateHelper.setupLargeCommonWidget<OrderAppWidget>(
                context,
                remoteViews,
                data
            )
        }
    }
}