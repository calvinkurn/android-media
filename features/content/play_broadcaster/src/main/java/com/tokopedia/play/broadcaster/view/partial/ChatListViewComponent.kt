package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.adapter.ChatListAdapter
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.ui.chat.ChatScrollDownView
import com.tokopedia.play_common.ui.chat.itemdecoration.PlayChatItemDecoration
import com.tokopedia.play_common.viewcomponent.ViewComponent
import timber.log.Timber

/**
 * Created by jegul on 09/06/20
 */
class ChatListViewComponent(
        container: ViewGroup
) : ViewComponent(container, R.id.chat_list) {

    private val chatAdapter = ChatListAdapter()

    private val rvChatList = findViewById<RecyclerView>(R.id.rv_chat_list)
    private val csDownView = findViewById<ChatScrollDownView>(R.id.csdown_view).apply {
        setOnClickListener {
            if (rvChatList.canScrollDown) {
                rvChatList.scrollToPosition(chatAdapter.lastIndex)
            }
        }
    }
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (recyclerView.canScrollDown && recyclerView.scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                if (isChatPositionBeyondOffset(recyclerView)) csDownView.show()
                else csDownView.hide()
            } else if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_SETTLING) {
                csDownView.apply { showIndicatorRed(false) }.hide()
            } else if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                if (!recyclerView.canScrollDown) csDownView.hide()
                else {
                    if (isChatPositionBeyondOffset(recyclerView)) csDownView.show()
                    else csDownView.hide()
                }
            }
        }
    }

    private val RecyclerView.canScrollDown: Boolean
        get() = canScrollVertically(1)

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            csDownView.showIndicatorRed(rvChatList.canScrollDown)
            if (!csDownView.isVisible) {
                rvChatList.post {
                    rvChatList.scrollToPosition(chatAdapter.lastIndex)
                }
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

    private fun isChatPositionBeyondOffset(recyclerView: RecyclerView): Boolean {
        val offset = recyclerView.computeVerticalScrollOffset()
        val range = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent()
        return offset + SCROLL_OFFSET_INDICATOR < range - 1
    }

    companion object {
        private const val SCROLL_OFFSET_INDICATOR = 90
    }
}