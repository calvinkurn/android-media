package com.tokopedia.chatbot.domain.pojo.dynamicattachment

import com.google.gson.annotations.SerializedName

data class ReplyBoxAttribute(
    @SerializedName("content_code")
    val contentCode: Int?,
    @SerializedName("render_target")
    val renderTarget: String?,
    @SerializedName("dynamic_content")
    val dynamicContent: String?,
    @SerializedName("user_id")
    val userId: Long?
)
