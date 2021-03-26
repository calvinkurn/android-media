package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatReplyPojo (
    @Expose
    @SerializedName("chatReplyChat")
    var data: ChatReplyData = ChatReplyData()
)

data class ChatReplyData(
        @Expose
        @SerializedName("msgID")
        var msgId: String = "",

        @Expose
        @SerializedName("senderID")
        var senderId: String = "0",

        @Expose
        @SerializedName("msg")
        var message: String = "",

        @Expose
        @SerializedName("replyTime")
        var replyTime: String = "",

        @Expose
        @SerializedName("from")
        var from: String = "",

        @Expose
        @SerializedName("role")
        var role: Int = 0,

        @Expose
        @SerializedName("attachment")
        var attachment: ChatReplyAttachment = ChatReplyAttachment()
)

data class ChatReplyAttachment(
        @Expose
        @SerializedName("id")
        var id: String = "0",

        @Expose
        @SerializedName("type")
        var type: Int = 0,

        @Expose
        @SerializedName("fallback")
        var fallback: ChatReplyFallback = ChatReplyFallback(),

        @Expose
        @SerializedName("attributes")
        var attributes: String = ""
)

data class ChatReplyFallback(
    @Expose
    @SerializedName("html")
    var html: String = "",

    @Expose
    @SerializedName("message")
    var message: String = ""
)