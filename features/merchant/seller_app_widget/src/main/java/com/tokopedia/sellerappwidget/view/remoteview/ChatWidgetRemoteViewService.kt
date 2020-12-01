package com.tokopedia.sellerappwidget.view.remoteview

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.model.ChatUiModel

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

        private var chats: ArrayList<ChatUiModel> = arrayListOf()

        init {
            val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
            chats = bundle?.getParcelableArrayList(Const.Extra.CHAT_ITEMS) ?: arrayListOf()
        }

        override fun onCreate() {

        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.saw_app_widget_chat_item_shimmer)
        }

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds(): Boolean = true

        override fun getViewAt(position: Int): RemoteViews {
            val chat = chats.getOrNull(position) ?: ChatUiModel()
            return RemoteViews(context.packageName, R.layout.saw_app_widget_chat_item).apply {

            }
        }

        override fun getCount(): Int = chats.size

        override fun getViewTypeCount(): Int = 1

        override fun onDataSetChanged() {

        }

        override fun onDestroy() {

        }
    }
}