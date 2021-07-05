package com.tokopedia.sellerappwidget.view.state.chat

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.*
import com.tokopedia.sellerappwidget.view.appwidget.ChatAppWidget
import com.tokopedia.sellerappwidget.view.model.CommonStateUiModel
import com.tokopedia.sellerappwidget.view.state.AppWidgetStateHelper

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object ChatWidgetStateHelper : AppWidgetStateHelper() {

    private val viewIds = listOf(
            R.id.containerSawNormalCommonState,
            R.id.containerSawLargeCommonState,
            R.id.containerSawChatListLoading,
            R.id.containerSawChatSuccessState
    )

    fun setupNormalWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        super.setupNormalCommonWidget<ChatAppWidget>(context, remoteViews, data)
    }

    fun setupLargeWidget(context: Context, remoteViews: RemoteViews, data: CommonStateUiModel) {
        super.setupLargeCommonWidget<ChatAppWidget>(context, remoteViews, data)
    }

    fun setLastUpdated(context: Context, remoteView: RemoteViews) {
        val updatedFmt = getWidgetLastUpdatedFmt(context, Const.SharedPrefKey.CHAT_LAST_UPDATED)
        val updated = context.getString(R.string.saw_updated)
        remoteView.setTextViewText(R.id.tvSawChatUpdated, "$updated $updatedFmt")
    }

    fun updateViewCommonState(remoteViews: RemoteViews, @WidgetSize widgetSize: String) {
        when (widgetSize) {
            WidgetSize.NORMAL -> remoteViews.setChatUiVisibility(R.id.containerSawNormalCommonState)
            else -> remoteViews.setChatUiVisibility(R.id.containerSawLargeCommonState)
        }
    }

    fun updateViewOnSuccess(remoteViews: RemoteViews) {
        remoteViews.setChatUiVisibility(R.id.containerSawChatSuccessState)
    }

    fun updateViewOnLoading(remoteViews: RemoteViews) {
        remoteViews.setChatUiVisibility(R.id.containerSawChatListLoading)
    }

    private fun RemoteViews.setChatUiVisibility(viewToVisible: Int) {
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