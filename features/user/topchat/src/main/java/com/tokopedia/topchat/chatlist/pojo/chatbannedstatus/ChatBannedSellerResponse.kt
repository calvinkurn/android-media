package com.tokopedia.topchat.chatlist.pojo.chatbannedstatus


import com.google.gson.annotations.SerializedName

data class ChatBannedSellerResponse(
    @SerializedName("chatBannedSeller")
    val chatBannedSeller: ChatBannedSeller = ChatBannedSeller()
)