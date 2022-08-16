package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.SerializedName

data class ChatExistingChat(
    @SerializedName("chatExistingChat")
    val chatExistingChat: ChatMessageId
)

data class ChatMessageId(
    @SerializedName("messageId")
    val messageId: String
)