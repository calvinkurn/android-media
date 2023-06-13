package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatUpdateTemplateResponse(
    @SerializedName("chatUpdateTemplate")
    val chatUpdateTemplate: ChatUpdateTemplate = ChatUpdateTemplate()
)

data class ChatUpdateTemplate(
    @SerializedName("success")
    var success: Int = 0
)
