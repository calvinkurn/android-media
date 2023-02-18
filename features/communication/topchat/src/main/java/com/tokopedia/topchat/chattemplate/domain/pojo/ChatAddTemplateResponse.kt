package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatAddTemplateResponse(
    @SerializedName("chatAddTemplate")
    val chatAddTemplate: ChatAddTemplate = ChatAddTemplate()
)

data class ChatAddTemplate(
    @SerializedName("success")
    var success: Int = 0
)
