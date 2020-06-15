package com.tokopedia.topchat.chatsearch.data

import com.google.gson.annotations.SerializedName

class GetMultiChatSearchResponse(
        @SerializedName("searchByName")
        val searchByName: ChatSearch = ChatSearch(),
        @SerializedName("searchByReply")
        val searchByReply: ChatSearch = ChatSearch()
) {

    val contactHasNext: Boolean get() = searchByName.contact.hasNext
    val contactSearchResults get() = searchByName.contact.searchResults
    val contactCount get() = searchByName.contact.count
}