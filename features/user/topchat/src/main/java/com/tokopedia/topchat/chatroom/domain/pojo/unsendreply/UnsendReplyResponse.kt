package com.tokopedia.topchat.chatroom.domain.pojo.unsendreply


import com.google.gson.annotations.SerializedName

data class UnsendReplyResponse(
    @SerializedName("unsendReply")
    var unsendReply: UnsendReply = UnsendReply()
)