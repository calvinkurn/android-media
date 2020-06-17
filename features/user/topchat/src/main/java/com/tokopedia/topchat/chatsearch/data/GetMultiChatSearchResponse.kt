package com.tokopedia.topchat.chatsearch.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatsearch.data.reply.ChatReply

class GetMultiChatSearchResponse(
        @SerializedName("searchByName")
        val searchByName: ChatSearch = ChatSearch(),
        @SerializedName("searchByReply")
        val searchByReply: ChatReply = ChatReply()
) {
    val replyHasNext: Boolean get() = searchByReply.replies.hasNext
    val contactHasNext: Boolean get() = searchByName.contact.hasNext
    val contactSearchResults get() = searchByName.contact.searchResults
    val contactCount get() = searchByName.contact.count
    val replySearchResults get() = searchByReply.replies.data
}