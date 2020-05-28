package com.tokopedia.topchat.chatroom.domain.pojo.chatattachment


import com.google.gson.annotations.SerializedName

data class ChatAttachmentResponse(
        @SerializedName("chatAttachments")
        val chatAttachments: ChatAttachments = ChatAttachments()
)