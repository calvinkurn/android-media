package com.tokopedia.sellerappwidget.view.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.executor.GetChatExecutor
import com.tokopedia.sellerappwidget.view.model.ChatItemUiModel
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
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
                GetChatExecutor.run(context, true)
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
            Const.Action.OPEN_APPLINK -> openAppLink(context, intent)
        }

        if (intent.action != AppWidgetManager.ACTION_APPWIDGET_UPDATE
                || intent.action != AppWidgetManager.ACTION_APPWIDGET_DISABLED) {
            AppWidgetHelper.setChatAppWidgetEnabled(context, true)
        }

        super.onReceive(context, intent)
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        initUserSession(context)
        userSession?.let {
            if (!it.isLoggedIn) {
                val awm = AppWidgetManager.getInstance(context)
                val ids = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)
                ChatWidgetNoLoginState.setupNoLoginState(context, awm, ids)
                super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
                return
            }
        }
        GetChatExecutor.run(context, true)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onEnabled(context: Context) {
        val cacheHandler = AppWidgetHelper.getCacheHandler(context)
        cacheHandler.putBoolean(Const.SharedPrefKey.CHAT_WIDGET_ENABLED, true)
        cacheHandler.applyEditor()
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        val cacheHandler = AppWidgetHelper.getCacheHandler(context)
        cacheHandler.putBoolean(Const.SharedPrefKey.CHAT_WIDGET_ENABLED, false)
        cacheHandler.applyEditor()
        super.onDisabled(context)
    }

    private fun openAppLink(context: Context, intent: Intent) {
        val applink = intent.data?.toString().orEmpty()
        val appWidgetTracking = AppWidgetTracking.getInstance(context)
        when (applink) {
            ApplinkConst.SellerApp.CENTRALIZED_PROMO -> {
                appWidgetTracking.sendEventClickCheckNowChatWidget()
            }
            ApplinkConst.LOGIN -> {
                appWidgetTracking.sendEventClickLoginNowChatWidget()
            }
            ApplinkConstInternalSellerapp.SELLER_HOME -> {
                appWidgetTracking.sendEventClickSellerIconChatWidget()
            }
            ApplinkConstInternalSellerapp.SELLER_HOME_CHAT -> {
                appWidgetTracking.sendEventClickShopNameChatWidget()
            }
        }
        AppWidgetHelper.openAppLink(context, intent)
    }

    private fun onChatItemClick(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
        val chatItemItem: ChatItemUiModel? = bundle?.getParcelable(Const.Extra.CHAT_ITEM)
        val chatId = chatItemItem?.messageId ?: 0
        val chatRoomIntent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT, chatId.toString()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventClickItemChatWidget()

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

        AppWidgetTracking.getInstance(context)
                .sendEventClickRefreshButtonChatWidget()

        GetChatExecutor.run(context, true)
    }

    private fun initUserSession(context: Context) {
        if (userSession == null) {
            userSession = UserSession(context)
        }
    }

    companion object {

        fun setOnSuccess(context: Context, chat: ChatUiModel) {
            val awm = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)

            val userSession: UserSessionInterface = UserSession(context)

            if (!getIsUserLoggedIn(context, awm, userSession, widgetIds)) {
                return
            }

            val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(context)

            widgetIds.forEach { widgetId ->
                when {
                    chat.chats.isEmpty() -> ChatWidgetEmptyState.setupEmptyState(context, remoteViews, widgetId)
                    else -> {
                        ChatWidgetSuccessState.setupSuccessState(context, remoteViews, userSession, chat, widgetId)
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

        fun showNoLoginState(context: Context) {
            val awm = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)
            ChatWidgetNoLoginState.setupNoLoginState(context, awm, widgetIds)
        }

        private fun getIsUserLoggedIn(context: Context, awm: AppWidgetManager, userSession: UserSessionInterface, widgetIds: IntArray): Boolean {
            if (!userSession.isLoggedIn) {
                ChatWidgetNoLoginState.setupNoLoginState(context, awm, widgetIds)
            }
            return userSession.isLoggedIn
        }
    }
}