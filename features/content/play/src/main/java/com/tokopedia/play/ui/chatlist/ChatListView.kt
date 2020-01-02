package com.tokopedia.play.ui.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.chatlist.adapter.ChatAdapter
import com.tokopedia.play.ui.chatlist.itemdecoration.ChatListItemDecoration
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.custom.ChatScrollDownView
import timber.log.Timber

/**
 * Created by jegul on 03/12/19
 */
class ChatListView(
        container: ViewGroup
) : UIView(container) {

    companion object {
        private const val SCROLL_OFFSET_INDICATOR = 50
    }

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_chat_list, container, true)
                    .findViewById(R.id.cl_chat_list)

    private val rvChatList: RecyclerView = view.findViewById(R.id.rv_chat_list)
    private val csDownView: ChatScrollDownView = view.findViewById<ChatScrollDownView>(R.id.csdown_view).apply {
        setOnClickListener {
            if (rvChatList.canScrollDown) rvChatList.smoothScrollToPosition(chatAdapter.lastIndex)
        }
    }
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (recyclerView.canScrollDown) {
                val offset = recyclerView.computeVerticalScrollOffset()
                val range = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent()
                if (offset + SCROLL_OFFSET_INDICATOR < range - 1) csDownView.show()
                else csDownView.hide()
            } else {
                csDownView.apply { showIndicatorRed(false) }.hide()
            }
        }
    }

    private val chatAdapter = ChatAdapter()

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            csDownView.showIndicatorRed(rvChatList.canScrollDown)
            if (!csDownView.isVisible) {
                rvChatList.postDelayed({
                    rvChatList.smoothScrollToPosition(chatAdapter.lastIndex)
                    Timber.tag("ChatList").d("Smooth Scroll to Posiiton ${chatAdapter.lastIndex}")
                }, 100)
            }
        }
    }

    init {
        rvChatList.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
            addOnScrollListener(scrollListener)
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

    fun getEstimatedYPos(): Int {
        return view.y.toInt()
    }

    private val RecyclerView.canScrollDown: Boolean
        get() = canScrollVertically(1)
}