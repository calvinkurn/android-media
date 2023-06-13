package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatDeleteTemplateResponse(
    @SerializedName("chatDeleteTemplate")
    val chatDeleteTemplate: ChatDeleteTemplate = ChatDeleteTemplate()
)

data class ChatDeleteTemplate(
    @SerializedName("success")
    var success: Int = 0
)
