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

    @SerializedName("stream_rtmp_sd")
    @Expose
    var streamRtmpStandard: String = ""

    @SerializedName("stream_rtmp_hd")
    @Expose
    var streamRtmpHigh: String = ""

    @SerializedName("stream_hls_sd")
    @Expose
    var streamHlsStandard: String = ""

    @SerializedName("stream_hls_hd")
    @Expose
    var streamHlsHigh: String = ""

    @SerializedName("is_active")
    @Expose
    var isActive: Boolean = false

    @SerializedName("is_live")
    @Expose
    var isLive: Boolean = false
}