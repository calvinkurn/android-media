package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.SerializedName

data class ChatGearChatListResponse(
    @SerializedName("chatGearChatList")
    val chatGearChatList: ChatGearChatList = ChatGearChatList()
)