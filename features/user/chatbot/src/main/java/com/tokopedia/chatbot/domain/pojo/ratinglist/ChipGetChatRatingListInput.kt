package com.tokopedia.chatbot.domain.pojo.ratinglist

import com.google.gson.annotations.SerializedName


data class ChipGetChatRatingListInput(
        @SerializedName("caseChatIDs")
        var caseChatIDs: String? = null
)