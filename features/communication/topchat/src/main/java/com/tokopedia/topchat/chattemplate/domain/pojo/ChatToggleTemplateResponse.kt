package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatToggleTemplateResponse(
    @SerializedName("chatToggleTemplate")
    val chatToggleTemplate: ChatToggleTemplate = ChatToggleTemplate()
)

data class ChatToggleTemplate(
    @SerializedName("success")
    var success: Int = 0
)
