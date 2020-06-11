package com.tokopedia.topchat.chatsearch.view.fragment

import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.EmptySearchChatViewHolder

interface ChatSearchFragmentListener : EmptySearchChatViewHolder.Listener {
    fun onClickContactLoadMore(query: String)
    fun showSearchBar()
}