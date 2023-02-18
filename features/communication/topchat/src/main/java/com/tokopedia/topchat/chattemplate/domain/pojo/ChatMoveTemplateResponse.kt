package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatMoveTemplateResponse(
    @SerializedName("chatMoveTemplate")
    val chatMoveTemplate: ChatMoveTemplate = ChatMoveTemplate()
)

data class ChatMoveTemplate(
    @SerializedName("success")
    val success: Int = 0
)
