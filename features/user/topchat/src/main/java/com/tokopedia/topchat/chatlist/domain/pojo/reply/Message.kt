package com.tokopedia.topchat.chatlist.domain.pojo.reply

import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName("censored_reply")
    val censoredReply: String = ""

    @SerializedName("original_reply")
    val originalReply: String = ""

    @SerializedName("timestamp")
    var timestamp: String = ""

    @SerializedName("timestamp_fmt")
    val timestampFmt: String = ""

    @SerializedName("timestamp_unix_nano")
    val timeStampUnixNano: String = ""

    @SerializedName("timestamp_unix")
    val timeStampUnix: String = ""
}