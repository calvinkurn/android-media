package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.chatlist.adapter.ChatAdapter
import com.tokopedia.play.ui.chatlist.itemdecoration.ChatListItemDecoration
import com.tokopedia.play.view.custom.ChatScrollDownView
import com.tokopedia.play.view.custom.MaximumHeightRecyclerView
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import timber.log.Timber

/**
 * Created by jegul on 03/08/20
 */
class ChatListViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val rvChatList: MaximumHeightRecyclerView = findViewById(R.id.rv_chat_list)
    private val csDownView: ChatScrollDownView = findViewById<ChatScrollDownView>(R.id.csdown_view).apply {
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

    private val chatAdapter = ChatAdapter()

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            csDownView.showIndicatorRed(rvChatList.canScrollDown)
            if (!csDownView.isVisible || chatAdapter.getItem(chatAdapter.lastIndex).isSelfMessage) {
                rvChatList.post {
                    rvChatList.scrollToPosition(chatAdapter.lastIndex)
                }
            }
        }
    }

    init {
        rvChatList.apply {
            val layoutMan = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutMan.stackFromEnd = true
            layoutManager = layoutMan
            itemAnimator = null

            adapter = chatAdapter
            addOnScrollListener(scrollListener)
            addItemDecoration(ChatListItemDecoration(context))
        }
        chatAdapter.registerAdapterDataObserver(adapterObserver)
    }

    fun showNewChat(chat: PlayChatUiModel) {
        chatAdapter.addChat(chat)
    }

    fun setChatList(chatList: List<PlayChatUiModel>) {
        chatAdapter.setChatList(chatList)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        chatAdapter.unregisterAdapterDataObserver(adapterObserver)
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