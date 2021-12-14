package com.tokopedia.topchat.chatroom.domain.pojo.unsendreply


import com.google.gson.annotations.SerializedName

data class UnsendReply(
    @SerializedName("success")
    var isSuccess: Boolean = false
)