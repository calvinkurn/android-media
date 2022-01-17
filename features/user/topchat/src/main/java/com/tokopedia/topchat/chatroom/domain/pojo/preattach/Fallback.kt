package com.tokopedia.topchat.chatroom.domain.pojo.preattach


import com.google.gson.annotations.SerializedName

data class Fallback(
    @SerializedName("html")
    var html: String = "",
    @SerializedName("message")
    var message: String = ""
)