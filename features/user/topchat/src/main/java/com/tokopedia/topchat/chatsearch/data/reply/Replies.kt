package com.tokopedia.topchat.chatsearch.data.reply


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel

data class Replies(
        @SerializedName("data")
        val `data`: List<ChatReplyUiModel> = listOf(),
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("count")
        val count: String = ""
)