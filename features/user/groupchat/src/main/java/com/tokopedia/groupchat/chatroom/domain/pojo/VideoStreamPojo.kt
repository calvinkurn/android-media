package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 19/06/19
 */
class VideoStreamParent {

    @SerializedName("video_stream")
    @Expose
    var videoStreamData = VideoStreamPojo()

}

class VideoStreamPojo {

    @SerializedName("orientation")
    @Expose
    var orientation: String = ""

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

    @SerializedName("is_active")
    @Expose
    var isActive: Boolean = false

    @SerializedName("is_live")
    @Expose
    var isLive: Boolean = false
}