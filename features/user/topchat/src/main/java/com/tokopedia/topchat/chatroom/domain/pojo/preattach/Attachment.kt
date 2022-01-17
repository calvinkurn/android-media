package com.tokopedia.topchat.chatroom.domain.pojo.preattach


import com.google.gson.annotations.SerializedName

data class Attachment(
    @SerializedName("attributes")
    var attributes: String = "",
    @SerializedName("fallback")
    var fallback: Fallback = Fallback(),
    @SerializedName("id")
    var id: String = "",
    @SerializedName("type")
    var type: Int = 0
)