package com.tokopedia.topchat.chatlist.domain.pojo.reply

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class Attachment (
    @SerializedName("id")
    var id: String = "",

    @SerializedName("type")
    var type: String = "",

    @SerializedName("attributes")
    var attributes: JsonObject = JsonObject(),

    @SerializedName("fallback_attachment")
    var fallbackAttachment: FallbackAttachment = FallbackAttachment()
)