package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatGearChatListResponse(
        @SerializedName("chatGearChatList")
        @Expose
        val chatGearChatList: ChatGearChatList = ChatGearChatList()
)