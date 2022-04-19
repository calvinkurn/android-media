package com.tokopedia.sellerappwidget.view.state.chat

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 02/12/20
 */

object ChatWidgetEmptyState {

    fun setupEmptyState(context: Context, remoteViews: RemoteViews, widgetId: Int) {
        val awm = AppWidgetManager.getInstance(context)
        val option = awm.getAppWidgetOptions(widgetId)
        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)

        ChatWidgetStateHelper.updateViewCommonState(remoteViews, widgetSize)

        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_chat_empty_state_title),
                description = context.getString(R.string.saw_chat_empty_state_description),
                imgUrl = Const.Images.COMMON_ON_EMPTY,
                ctaText = context.getString(R.string.saw_check_now),
                appLink = ApplinkConst.SellerApp.CENTRALIZED_PROMO
        )
        when (widgetSize) {
            WidgetSize.NORMAL -> ChatWidgetStateHelper.setupNormalWidget(context, remoteViews, data)
            else -> ChatWidgetStateHelper.setupLargeWidget(context, remoteViews, data)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventImpressionEmptyStateChatWidget()
    }
}