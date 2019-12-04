package com.tokopedia.play.ui.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.chatlist.adapter.ChatAdapter
import com.tokopedia.play.ui.chatlist.itemdecoration.ChatListItemDecoration
import com.tokopedia.play.ui.chatlist.model.PlayChat

/**
 * Created by jegul on 03/12/19
 */
class ChatListView(
        container: ViewGroup
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_chat_list, container, true)
                    .findViewById(R.id.rv_chat_list)

    private val rvChatList: RecyclerView = view as RecyclerView

    private val chatAdapter = ChatAdapter()

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            rvChatList.postDelayed ({
                if (rvChatList.canScrollVertically(1)) {
                    rvChatList.smoothScrollToPosition(chatAdapter.itemCount - 1)
                }
            }, 500)
        }
    }

    init {
        rvChatList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
            addItemDecoration(ChatListItemDecoration(view.context))
        }
        chatAdapter.registerAdapterDataObserver(adapterObserver)
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    fun showChat(chat: PlayChat) {
        chatAdapter.addChat(chat)
    }

    fun onDestroy() {
        chatAdapter.unregisterAdapterDataObserver(adapterObserver)
    }
}