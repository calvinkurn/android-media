package com.tokopedia.sellerappwidget.view.remoteview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.model.ChatItemUiModel

/**
 * Created By @ilhamsuaib on 01/12/20
 */

class ChatWidgetRemoteViewService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ChatAppWidgetFactory(applicationContext, intent)
    }

    class ChatAppWidgetFactory(
            private val context: Context,
            intent: Intent
    ) : RemoteViewsFactory {

        private var chatItems: ArrayList<ChatItemUiModel> = arrayListOf()

        init {
            val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
            chatItems = bundle?.getParcelableArrayList(Const.Extra.CHAT_ITEMS) ?: arrayListOf()
        }

        override fun onCreate() {

        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.saw_app_widget_chat_item_shimmer).apply {
                setInt(R.id.sawChatShimmerName, Const.Method.SET_VISIBILITY, View.INVISIBLE)
                setInt(R.id.sawChatShimmerLastMsg, Const.Method.SET_VISIBILITY, View.INVISIBLE)
                setInt(R.id.horLineSawChatItem, Const.Method.SET_VISIBILITY, View.INVISIBLE)
            }
        }

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds(): Boolean = true

        override fun getViewAt(position: Int): RemoteViews {
            val chat = chatItems.getOrNull(position) ?: ChatItemUiModel()
            return RemoteViews(context.packageName, R.layout.saw_app_widget_chat_item).apply {
                setTextViewText(R.id.tvSawChatItemUserName, chat.userDisplayName)
                setTextViewText(R.id.tvSawChatItemMessage, chat.lastMessage)
                setTextViewText(R.id.tvSawChatItemTime, chat.lastReplyTime)

                val horLineVisibility = if (position == chatItems.size.minus(1)) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
                setInt(R.id.horLineSawChatListItem, Const.Method.SET_VISIBILITY, horLineVisibility)

                //handle on item click
                val fillIntent = Intent().apply {
                    putExtra(Const.Extra.BUNDLE, Bundle().apply {
                        putParcelable(Const.Extra.CHAT_ITEM, chat)
                    })
                }
                setOnClickFillInIntent(R.id.containerSawChatItem, fillIntent)
            }
        }

        override fun getCount(): Int = chatItems.size

        override fun getViewTypeCount(): Int = 1

        override fun onDataSetChanged() {

        }

        override fun onDestroy() {

        }
    }
}