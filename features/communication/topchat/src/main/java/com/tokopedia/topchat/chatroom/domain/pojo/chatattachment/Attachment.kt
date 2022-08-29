package com.tokopedia.topchat.chatroom.domain.pojo.chatattachment


import com.google.gson.annotations.SerializedName

open class Attachment(
        @SerializedName("attributes")
        var attributes: String = "",
        @SerializedName("fallback")
        val fallback: Fallback = Fallback(),
        @SerializedName("id")
        val id: String = "",
        @SerializedName("isActual")
        val isActual: Boolean = false,
        @SerializedName("type")
        val type: Int = 0
) {

    var parsedAttributes: Any? = null

}