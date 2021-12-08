package com.tokopedia.topchat.chatroom.domain.pojo.unsendreply


import com.google.gson.annotations.SerializedName

data class UnsendReply(
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false
)