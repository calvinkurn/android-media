package com.tokopedia.topchat.chatlist.pojo.whitelist


import com.google.gson.annotations.SerializedName

data class ChatWhitelistFeatureResponse(
    @SerializedName("chatWhitelistFeature")
    val chatWhitelistFeature: ChatWhitelistFeature = ChatWhitelistFeature()
)