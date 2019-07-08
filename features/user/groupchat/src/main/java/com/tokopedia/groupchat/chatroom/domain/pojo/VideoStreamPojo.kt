package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 19/06/19
 */
class VideoStreamPojo {

    @SerializedName("orientation")
    @Expose
    var orientation: String = ""

    @SerializedName("data")
    @Expose
    var streamData: StreamData = StreamData()

    @SerializedName("is_active")
    @Expose
    var isActive: Boolean = false

    @SerializedName("is_live")
    @Expose
    var isLive: Boolean = false
}

class StreamData {
    @SerializedName("android_stream_hd")
    @Expose
    var androidStreamHD: String = ""

    @SerializedName("android_stream_sd")
    @Expose
    var androidStreamSD: String = ""

    @SerializedName("ios_stream_hd")
    @Expose
    var iosStreamHD: String = ""

    @SerializedName("ios_stream_sd")
    @Expose
    var iosStreamSD: String = ""
}