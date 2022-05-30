package com.tokopedia.topchat.chatlist.domain.pojo.reply

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class Attachment (
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("attributes")
    var attributes: JsonObject? = null,

    @SerializedName("fallback_attachment")
    var fallbackAttachment: FallbackAttachment? = null
)