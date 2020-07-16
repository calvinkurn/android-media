package com.tokopedia.discovery2.data.customtopchatdatamodel

import com.google.gson.annotations.SerializedName

data class CustomChatResponse(
        @SerializedName("chatExistingChat")
        val chatExistingChat: ChatExistingChat? = null
)