package com.tokopedia.topchat.chatlist.domain.pojo.reply

import com.google.gson.annotations.SerializedName

data class FallbackAttachment (
    @SerializedName("message")
    var message: String? = null,

    @SerializedName("url")
    var url: String? = null,

    @SerializedName("span")
    var span: String? = null,

    @SerializedName("html")
    var html: String? = null
)