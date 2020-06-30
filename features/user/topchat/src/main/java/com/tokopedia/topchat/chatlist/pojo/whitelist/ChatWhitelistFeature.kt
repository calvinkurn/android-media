package com.tokopedia.topchat.chatlist.pojo.whitelist


import com.google.gson.annotations.SerializedName

data class ChatWhitelistFeature(
    @SerializedName("isWhitelist")
    val isWhitelist: Boolean = false
)