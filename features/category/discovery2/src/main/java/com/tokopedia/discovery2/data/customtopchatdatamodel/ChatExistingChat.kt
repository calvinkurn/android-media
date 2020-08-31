package com.tokopedia.discovery2.data.customtopchatdatamodel

import com.google.gson.annotations.SerializedName

data class ChatExistingChat(
        @SerializedName("messageId")
        val messageId: Int? = 0
)