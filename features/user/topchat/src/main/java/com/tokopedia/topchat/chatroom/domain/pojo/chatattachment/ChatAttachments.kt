package com.tokopedia.topchat.chatroom.domain.pojo.chatattachment


import com.google.gson.annotations.SerializedName

data class ChatAttachments(
        @SerializedName("list")
        val list: List<Attachment> = listOf()
)