package com.tokopedia.topchat.chatlist.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 2019-08-08
 */
data class ChatListPojo(
    @SerializedName("chatListMessage")
    var data: ChatListDataPojo = ChatListDataPojo()
) {
    data class ChatListDataPojo(
        @SerializedName("list")
        var list: List<ItemChatListPojo> = arrayListOf(),
        @SerializedName("hasNext")
        var hasNext: Boolean = false,
        @SerializedName("pagingNext")
        var pagingNext: Boolean = false,
        @SerializedName("showTimeMachine")
        var showTimeMachine: Int = 0
    )
}

data class ChatListParam(
    val page: Int,
    val filter: String,
    val tab: String
)

data class ChatListResponse(
    val chatListPojo: ChatListPojo,
    val pinned: List<String>,
    val unpinned: List<String>
)
