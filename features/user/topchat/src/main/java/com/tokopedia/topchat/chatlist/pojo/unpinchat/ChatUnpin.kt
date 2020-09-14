package com.tokopedia.topchat.chatlist.pojo.unpinchat


import com.google.gson.annotations.SerializedName

data class ChatUnpin(
        @SerializedName("success")
        val success: Boolean = false
)