package com.tokopedia.sellerappwidget.view.state.chat

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel

/**
 * Created By @ilhamsuaib on 01/12/20
 */

object ChatWidgetNoLoginState {

    fun setupNoLoginState(context: Context, awm: AppWidgetManager, ids: IntArray) {
        val remoteView = AppWidgetHelper.getChatWidgetRemoteView(context)
        with(remoteView) {
            ids.forEach {
                val option = awm.getAppWidgetOptions(it)
                val widgetSize = AppWidgetHelper.getAppWidgetSize(option)
                ChatWidgetStateHelper.updateViewCommonState(remoteView, widgetSize)

                when (widgetSize) {
                    WidgetSize.NORMAL -> setupViewNormalWidget(context, remoteView, it)
                    else -> setupViewLargeWidget(context, remoteView, it)
                }

                awm.updateAppWidget(it, this)
            }
        }

        AppWidgetTracking.getInstance(context)
                .sendEventImpressionNoLoginStateChatWidget()
    }

    private fun setupViewNormalWidget(context: Context, remoteView: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_login_tokopedia_seller_large),
                description = context.getString(R.string.saw_no_login_check_chat_description),
                imgUrl = Const.Images.COMMON_NO_LOGIN,
                ctaText = context.getString(R.string.saw_login_now),
                appLink = ApplinkConst.LOGIN
        )
        ChatWidgetStateHelper.setupNormalWidget(context, remoteView, data)
        remoteView.setInt(R.id.btnSawNormalRefreshCommon, Const.Method.SET_VISIBILITY, View.INVISIBLE)
    }

    private fun setupViewLargeWidget(context: Context, remoteView: RemoteViews, widgetId: Int) {
        val data = CommonStateUiModel(
                widgetId = widgetId,
                title = context.getString(R.string.saw_login_tokopedia_seller_large),
                description = context.getString(R.string.saw_no_login_check_chat_description),
                imgUrl = Const.Images.COMMON_NO_LOGIN,
                ctaText = context.getString(R.string.saw_login_now),
                appLink = ApplinkConst.LOGIN
        )
        ChatWidgetStateHelper.setupLargeWidget(context, remoteView, data)
        remoteView.setInt(R.id.btnSawLargeRefreshCommon, Const.Method.SET_VISIBILITY, View.INVISIBLE)
    }
}