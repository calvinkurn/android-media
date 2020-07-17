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
//                Toast.makeText(context, "Load more top", Toast.LENGTH_SHORT).show()
        }
        if (hasNextAfterPage && !isLoadingBottom && shouldLoadMoreDown(firstVisiblePosition)) {
//                Toast.makeText(context, "Load more down", Toast.LENGTH_SHORT).show()
            loadMoreDown()
        }
//        if (dy < 0) {
//            // scroll down
//            return
//        }
//        if (dy > 0) {
//            // scroll down
//            return
//        }
//        if (dy == 0) {
//            if (shouldLoadMoreTop(lastVisiblePosition, totalItem, hasNextPage)) {
////                Toast.makeText(context, "Load more top", Toast.LENGTH_SHORT).show()
//            }
//            if (shouldLoadMoreDown(firstVisiblePosition, hasPreviousPage)) {
////                Toast.makeText(context, "Load more down", Toast.LENGTH_SHORT).show()
//            }
//            return
//        }
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

    fun finishTopLoadingState() {
        isLoadingTop = false
    }
}