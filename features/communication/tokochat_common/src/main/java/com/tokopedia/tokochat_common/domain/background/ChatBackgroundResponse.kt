package com.tokopedia.tokochat_common.domain.background

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatBackgroundResponse(
        @SerializedName("chatBackground")
        @Expose
        val chatBackground: ChatBackground = ChatBackground()
)
