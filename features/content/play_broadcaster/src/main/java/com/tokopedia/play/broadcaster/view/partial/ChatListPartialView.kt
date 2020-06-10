package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.adapter.ChatListAdapter
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.ui.chat.ChatScrollDownView
import com.tokopedia.play_common.ui.chat.itemdecoration.PlayChatItemDecoration
import timber.log.Timber

/**
 * Created by jegul on 09/06/20
 */
class ChatListPartialView(
        container: ViewGroup
) : PartialView(container, R.id.chat_list) {

    private val chatAdapter = ChatListAdapter()

    private val rvChatList = rootView.findViewById<RecyclerView>(R.id.rv_chat_list)
    private val csDownView = rootView.findViewById<ChatScrollDownView>(R.id.csdown_view).apply {
        setOnClickListener {
            if (rvChatList.canScrollDown) rvChatList.smoothScrollToPosition(chatAdapter.lastIndex)
        }
    }
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (recyclerView.canScrollDown) {
                val offset = recyclerView.computeVerticalScrollOffset()
                val range = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent()
                if (offset + SCROLL_OFFSET_INDICATOR < range - 1) csDownView.visible()
                else csDownView.gone()
            } else {
                csDownView.apply { showIndicatorRed(false) }.gone()
            }
        }
    }

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            csDownView.showIndicatorRed(rvChatList.canScrollDown)
            if (!csDownView.isVisible) {
                rvChatList.postDelayed({
                    rvChatList.smoothScrollToPosition(chatAdapter.lastIndex)
                    Timber.tag("ChatList").d("Smooth Scroll to Position ${chatAdapter.lastIndex}")
                }, 100)
            }
        }
    }

    init {
        setupChatList()
    }

    fun showNewChat(chat: PlayChatUiModel) {
        chatAdapter.addChat(chat)
    }

    fun setChatList(chatList: List<PlayChatUiModel>) {
        chatAdapter.setChatList(chatList)
    }

    private fun setupChatList() {
        rvChatList.adapter = chatAdapter
        rvChatList.addItemDecoration(PlayChatItemDecoration(rvChatList.context))
        rvChatList.addOnScrollListener(scrollListener)

        chatAdapter.registerAdapterDataObserver(adapterObserver)
    }

    private val RecyclerView.canScrollDown: Boolean
        get() = canScrollVertically(1)

    companion object {
        private const val SCROLL_OFFSET_INDICATOR = 50
    }
}