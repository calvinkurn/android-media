package com.tokopedia.sellerappwidget.view.state.chat

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.common.registerAppLinkIntent
import com.tokopedia.sellerappwidget.view.appwidget.ChatAppWidget
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.remoteview.ChatWidgetRemoteViewService
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 02/12/20
 */

object ChatWidgetSuccessState {

    fun setupSuccessState(context: Context, remoteViews: RemoteViews, userSession: UserSessionInterface, chats: List<ChatUiModel>, widgetId: Int) {
        val awm = AppWidgetManager.getInstance(context)
        val option = awm.getAppWidgetOptions(widgetId)
        ChatWidgetStateHelper.updateViewOnSuccess(remoteViews)

        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)

        val totalChats = "${chats.size} " + context.getString(R.string.saw_new_chat)
        with(remoteViews) {
            setTextViewText(R.id.tvSawChatTotalChat, totalChats)
            setTextViewText(R.id.tvSawChatShopName, userSession.shopName)

            //setup chat list
            val randomNumber = (Math.random() * 10000).toInt()
            val intent = Intent(context, ChatWidgetRemoteViewService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId + randomNumber)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                putExtra(Const.Extra.BUNDLE, Bundle().also {
                    it.putParcelableArrayList(Const.Extra.CHAT_ITEMS, ArrayList(chats))
                })
            }
            setRemoteAdapter(R.id.lvSawChatList, intent)

            //setup list view item click event
            val itemIntent = Intent(context, ChatAppWidget::class.java).apply {
                action = Const.Action.ITEM_CLICK
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            val itemPendingIntent = PendingIntent.getBroadcast(context, 0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setPendingIntentTemplate(R.id.lvSawChatList, itemPendingIntent)

            //load app icon
            Utils.getAppIcon(context)?.let {
                Utils.loadImageIntoAppWidget(context, this, R.id.imgSawChatAppIcon, it, widgetId)
            }

            //setup refresh button
            setInt(R.id.btnSawChatRefresh, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)
            ChatWidgetStateHelper.setupRefreshIntent<ChatAppWidget>(context, remoteViews, R.id.btnSawChatRefresh, widgetId)

            registerAppLinkIntent(context, R.id.tvSawChatShopName, ApplinkConstInternalSellerapp.SELLER_HOME_CHAT, widgetId)
            registerAppLinkIntent(context, R.id.tvSawChatTotalChat, ApplinkConstInternalSellerapp.SELLER_HOME_CHAT, widgetId)
            registerAppLinkIntent(context, R.id.imgSawChatAppIcon, ApplinkConst.SellerApp.SELLER_APP_HOME, widgetId)
        }
    }
}