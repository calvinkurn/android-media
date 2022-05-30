package com.tokopedia.topchat.chatlist.domain.pojo.unpinchat


import com.google.gson.annotations.SerializedName

data class ChatUnpin(
        @SerializedName("success")
        val success: Boolean = false
)