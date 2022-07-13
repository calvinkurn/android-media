package com.tokopedia.topchat.chatsearch.view.fragment

import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.EmptySearchChatViewHolder

interface ChatSearchFragmentListener : EmptySearchChatViewHolder.Listener {
    fun onClickContactLoadMore(query: String, firstPageContacts: GetChatSearchResponse)
    fun showSearchBar()
    fun hideKeyboard()
}