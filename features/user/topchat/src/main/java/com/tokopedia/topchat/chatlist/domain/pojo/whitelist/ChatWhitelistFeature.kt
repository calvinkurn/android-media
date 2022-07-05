package com.tokopedia.topchat.chatlist.domain.pojo.whitelist


import com.google.gson.annotations.SerializedName

data class ChatWhitelistFeature(
    @SerializedName("isWhitelist")
    var isWhitelist: Boolean = false
)