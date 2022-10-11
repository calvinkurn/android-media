package com.tokopedia.topchat.chatlist.domain.pojo.reply

import com.google.gson.annotations.SerializedName

data class FallbackAttachment (
    @SerializedName("message")
    var message: String = "",

    @SerializedName("url")
    var url: String = "",

    @SerializedName("span")
    var span: String = "",

    @SerializedName("html")
    var html: String = ""
)