package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.domain.pojo.ChatItemPojo

/**
 * @author : Steven 2019-08-08
 */
data class ChatListPojo(
        @SerializedName("chatListMessage")
        @Expose
        var data: ChatListDataPojo = ChatListDataPojo()
)


data class ChatListDataPojo (
        @SerializedName("list")
        @Expose
        var list: List<ItemChatListPojo> = arrayListOf(),
        @SerializedName("hasNext")
        @Expose
        var hasNext: Boolean = false,
        @SerializedName("pagingNext")
        @Expose
        var pagingNext: Boolean = false,
        @SerializedName("showTimeMachine")
        @Expose
        var showTimeMachine: Int = 0

)