package com.tokopedia.chatbot.chatbot2.data.ratinglist

import com.google.gson.annotations.SerializedName

data class ChipGetChatRatingListInput(
    @SerializedName("list")
    val list: MutableList<ChatRating> = mutableListOf()
) {
    data class ChatRating(
        @SerializedName("attachmentType")
        val attachmentType: Long?,
        @SerializedName("caseChatID")
        val caseChatID: String?
    )
}
