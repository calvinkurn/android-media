package com.tokopedia.sellerappwidget.view.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.service.GetChatService
import com.tokopedia.sellerappwidget.view.state.chat.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 01/12/20
 */

class ChatAppWidget : AppWidgetProvider() {

    private var userSession: UserSessionInterface? = null

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        initUserSession(context)
        userSession?.let {
            if (it.isLoggedIn) {
                showLoadingState(context, appWidgetManager, appWidgetIds)
                GetChatService.startService(context)
            } else {
                ChatWidgetNoLoginState.setupNoLoginState(context, appWidgetManager, appWidgetIds)
            }
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val awm = AppWidgetManager.getInstance(context)
                val ids = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)
                if (ids.isNotEmpty()) {
                    onUpdate(context, awm, ids)
                }
            }
            Const.Action.REFRESH -> refreshWidget(context)
            Const.Action.ITEM_CLICK -> onChatItemClick(context, intent)
            Const.Action.OPEN_APPLINK -> AppWidgetHelper.openAppLink(context, intent)
        }
        super.onReceive(context, intent)
    }

    private fun showLoadingState(context: Context, awm: AppWidgetManager, ids: IntArray) {
        val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(context)
        ids.forEach {
            ChatWidgetStateHelper.updateViewOnLoading(remoteViews)
            ChatWidgetLoadingState.setupLoadingState(awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }
    }

    private fun onChatItemClick(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
        val chatItem: ChatUiModel? = bundle?.getParcelable(Const.Extra.CHAT_ITEM)
        val chatId = chatItem?.messageId ?: 0
        val chatRoomIntent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT, chatId.toString()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chatRoomIntent)
    }

    private fun refreshWidget(context: Context) {
        initUserSession(context)
        userSession?.let {
            if (!it.isLoggedIn) {
                val awm = AppWidgetManager.getInstance(context)
                val appWidgetIds = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)
                ChatWidgetNoLoginState.setupNoLoginState(context, awm, appWidgetIds)
                return
            }
        }
        GetChatService.startService(context)
    }

    private fun initUserSession(context: Context) {
        if (userSession == null) {
            userSession = UserSession(context)
        }
    }

    companion object {

        fun setOnSuccess(context: Context, chats: List<ChatUiModel>) {
            val awm = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)

            val userSession: UserSessionInterface = UserSession(context)

            if (!getIsUserLoggedIn(context, awm, userSession, widgetIds)) {
                return
            }

            val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(context)

            widgetIds.forEach { widgetId ->
                when {
                    chats.isEmpty() -> ChatWidgetEmptyState.setupEmptyState(context, remoteViews, widgetId)
                    else -> {
                        ChatWidgetSuccessState.setupSuccessState(context, remoteViews, userSession, chats, widgetId)
                    }
                }

                ChatWidgetStateHelper.setLastUpdated(context, remoteViews)
                awm.updateAppWidget(widgetId, remoteViews)
            }
        }

        fun setOnError(context: Context) {
            val awm = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)

            val userSession: UserSessionInterface = UserSession(context)

            if (!getIsUserLoggedIn(context, awm, userSession, widgetIds)) {
                return
            }

            val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(context)

            widgetIds.forEach {
                ChatWidgetErrorState.setupErrorState(context, awm, remoteViews, it)
                awm.updateAppWidget(it, remoteViews)
            }
        }

        private fun getIsUserLoggedIn(context: Context, awm: AppWidgetManager, userSession: UserSessionInterface, widgetIds: IntArray): Boolean {
            if (!userSession.isLoggedIn) {
                ChatWidgetNoLoginState.setupNoLoginState(context, awm, widgetIds)
            }
            return userSession.isLoggedIn
        }
    }
}