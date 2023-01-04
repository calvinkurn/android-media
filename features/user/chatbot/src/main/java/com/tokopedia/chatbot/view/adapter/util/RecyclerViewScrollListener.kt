package com.tokopedia.chatbot.view.adapter.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.tokopedia.chat_common.domain.pojo.ChatReplies

abstract class RecyclerViewScrollListener(
    private val layoutManager : LinearLayoutManager?
) : RecyclerView.OnScrollListener() {

    private val threshold = 2
    //It enables the data loading from getTopChatHistory -> has Old Message History
    private var hasNextPage = false
    //It enables the data loading from getBottomChat -> move towards latest msg
    var hasNextAfterPage = false
        get() = field
    private var isLoadingTop = false
    private var isLoadingBottom = false

    abstract fun loadMoreTop()

    abstract fun loadMoreDown()

    abstract fun scrollDone()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val totalItem = layoutManager?.itemCount ?: return

        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

        if (isThisValidForShowingTopChat(lastVisiblePosition, totalItem)){
            isLoadingTop = true
            loadMoreTop()
        }
        if (isThisValidForShowingBottomChat(firstVisiblePosition)){
            isLoadingBottom = true
            loadMoreDown()
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            SCROLL_STATE_IDLE -> {
                scrollDone()
            }
        }
        super.onScrollStateChanged(recyclerView, newState)
    }

    private fun isThisValidForShowingTopChat(lastVisiblePosition: Int, totalItem: Int): Boolean {
        return (hasNextPage && !isLoadingTop && shouldLoadMoreTop(lastVisiblePosition, totalItem))
    }

    private fun isThisValidForShowingBottomChat(firstVisiblePosition: Int): Boolean{
        return (hasNextAfterPage && !isLoadingBottom && shouldLoadMoreDown(firstVisiblePosition))
    }

    private fun shouldLoadMoreTop(lastVisiblePosition: Int,totalItem: Int): Boolean{
        return (lastVisiblePosition + threshold) >= totalItem
    }

    private fun shouldLoadMoreDown(firstVisiblePosition: Int): Boolean {
        return (firstVisiblePosition - threshold) < 0
    }

    fun updateHasNextState(chat: ChatReplies){
        hasNextPage = chat.hasNext
    }

    fun updateHasNextAfterState(chat: ChatReplies){
        hasNextAfterPage = chat.hasNextAfter
    }

    fun finishTopLoadingState(){
        isLoadingTop = false
    }

    fun finishBottomLoadingState(){
        isLoadingBottom = false
    }

    fun reset(){
        hasNextPage = false
        hasNextAfterPage = false
        isLoadingTop = false
        isLoadingBottom = false
    }
}
