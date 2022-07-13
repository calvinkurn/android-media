package com.tokopedia.topchat.chatlist.domain.pojo.whitelist


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeature

data class ChatWhitelistFeatureResponse(
    @SerializedName("chatWhitelistFeature")
    val chatWhitelistFeature: ChatWhitelistFeature = ChatWhitelistFeature()
)