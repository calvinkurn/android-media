package com.tokopedia.videouploader.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 14/03/19.
 * {
"video_id": "1f4e5374625447f5b141ccdb7ab38097",
"media_type": "",
"duration": "0.0",
"cover_url": "",
"title": "",
"create_time": "2019-03-18T06:19:29Z",
"playback_list": [{
"id": 0,
"format": "",
"height": 0,
"width": 0,
"duration": "0.0",
"size": 0,
"create_time": "2019-03-18T06:19:29Z",
"status": "Uploading",
"definition": "",
"play_url": "https://vod.tokopedia.net/customerTrans/add4fb0c02a63644b1b88a0e133fe9f4/460283f5-1698f747bfc-0004-4b2d-ac1-3aaa1.mp4",
"stream_type": ""
}]
}
 */

data class DefaultUploadVideoResponse (
    @SerializedName("video_id")
    @Expose
    var videoId: String = "",
    @SerializedName("media_type")
    @Expose
    var mediaType: String = "",
    @SerializedName("duration")
    @Expose
    var duration: String = "",
    @SerializedName("cover_url")
    @Expose
    var coverUrl: String = "",
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("create_time")
    @Expose
    var createTime: String = "",
    @SerializedName("playback_list")
    @Expose
    var playbackList: ArrayList<Playback> = ArrayList()
    )

data class Playback (
        @SerializedName("format")
        @Expose
        var format: String = "",
        @SerializedName("height")
        @Expose
        var height: Int = 0,
        @SerializedName("width")
        @Expose
        var width: Int = 0,
        @SerializedName("duration")
        @Expose
        var duration: String = "",
        @SerializedName("size")
        @Expose
        var size: Int = 0,
        @SerializedName("create_time")
        @Expose
        var createTime: String = "",
        @SerializedName("status")
        @Expose
        var status: String = "",
        @SerializedName("definition")
        @Expose
        var definition: String = "",
        @SerializedName("play_url")
        @Expose
        var url: String = "",
        @SerializedName("stream_type")
        @Expose
        var streamType: String = ""
)