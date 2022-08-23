package com.tokopedia.topchat.chatroom.view.adapter.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.domain.pojo.ChatReplies

abstract class LoadMoreTopBottomScrollListener(
        private val layoutManager: LinearLayoutManager?
) : RecyclerView.OnScrollListener() {

    private val treshold = 2
    private var hasNextPage = false
    private var hasNextAfterPage = false
    private var isLoadingTop = false
    private var isLoadingBottom = false

    abstract fun loadMoreTop()

    abstract fun loadMoreDown()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val totalItem = layoutManager?.itemCount ?: return
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        if (hasNextPage && !isLoadingTop && shouldLoadMoreTop(lastVisiblePosition, totalItem)) {
            isLoadingTop = true
            loadMoreTop()
        }
        if (hasNextAfterPage && !isLoadingBottom && shouldLoadMoreDown(firstVisiblePosition)) {
            isLoadingBottom = true
            loadMoreDown()
        }
    }

    private fun shouldLoadMoreTop(lastVisiblePosition: Int, totalItem: Int): Boolean {
        return (lastVisiblePosition + treshold) >= totalItem
    }

    private fun shouldLoadMoreDown(firstVisiblePosition: Int): Boolean {
        return (firstVisiblePosition - treshold) < 0
    }

    fun updateHasNextState(chat: ChatReplies) {
        hasNextPage = chat.hasNext
    }

    fun updateHasNextAfterState(chat: ChatReplies) {
        hasNextAfterPage = chat.hasNextAfter
    }

    fun finishTopLoadingState() {
        isLoadingTop = false
    }

    fun finishBottomLoadingState() {
        isLoadingBottom = false
    }

    fun reset() {
        hasNextPage = false
        hasNextAfterPage = false
        isLoadingTop = false
        isLoadingBottom = false
    }
}