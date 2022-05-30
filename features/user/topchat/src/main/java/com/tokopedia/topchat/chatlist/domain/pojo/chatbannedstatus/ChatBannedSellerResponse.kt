package com.tokopedia.topchat.chatlist.domain.pojo.chatbannedstatus


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatlist.domain.pojo.chatbannedstatus.ChatBannedSeller

data class ChatBannedSellerResponse(
    @SerializedName("chatBannedSeller")
    val chatBannedSeller: ChatBannedSeller = ChatBannedSeller()
)