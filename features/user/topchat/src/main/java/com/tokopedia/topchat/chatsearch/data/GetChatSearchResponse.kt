package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class GetChatSearchResponse(
        @SerializedName("chatSearch")
        val chatSearch: ChatSearch = ChatSearch()
) {
    val searchResults get() = chatSearch.contact.searchResults
    val contactCount get() = chatSearch.contact.count
}