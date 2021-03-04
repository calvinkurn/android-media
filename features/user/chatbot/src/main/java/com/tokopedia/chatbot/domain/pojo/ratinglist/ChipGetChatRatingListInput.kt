package com.tokopedia.chatbot.domain.pojo.ratinglist

import com.google.gson.annotations.SerializedName

data class ChipGetChatRatingListInput(
    @SerializedName("list")
    val list: MutableList<ChatRating> = mutableListOf()
) {
    data class ChatRating(
        @SerializedName("attachmentType")
        val attachmentType: Int?,
        @SerializedName("caseChatID")
        val caseChatID: String?
    )
}
