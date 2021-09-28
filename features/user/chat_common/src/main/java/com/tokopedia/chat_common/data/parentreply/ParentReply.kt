package com.tokopedia.chat_common.data.parentreply

import com.google.gson.annotations.SerializedName

data class ParentReply(
    @SerializedName("main_text")
    val mainText: String = "",
    @SerializedName("sub_text")
    val subText: String = ""
)