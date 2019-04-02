package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by stevenfredian on 11/1/17.
 */

data class MessagePojo(

    @SerializedName("censored_reply")
    @Expose
    val censoredReply: String = "",
    @SerializedName("original_reply")
    @Expose
    val originalReply: String = "",
    @SerializedName("timestamp")
    @Expose
    var timestamp: String = "",
    @SerializedName("timestamp_fmt")
    @Expose
    val timestampFmt: String = "",
    @SerializedName("timestamp_unix_nano")
    @Expose
    val timeStampUnixNano: String = "",
    @SerializedName("timestamp_unix")
    @Expose
    val timeStampUnix: String = ""
)
