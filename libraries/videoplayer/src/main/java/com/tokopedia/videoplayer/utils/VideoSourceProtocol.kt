package com.tokopedia.videoplayer.utils

sealed class VideoSourceProtocol {

    object Http: VideoSourceProtocol()
    object Rtmp: VideoSourceProtocol()
    object File: VideoSourceProtocol()

    companion object {
        const val http = "http"
        const val https = "https"
        const val rtmp = "rtmp"
        const val file = "file"

        fun protocol(source: String): VideoSourceProtocol {
            if (!source.contains(":")) {
                throw Exception("TkpdVideoPlayer: invalid source media format.")
            } else {
                val url = source.split(":").first()
                return when (url) {
                    https -> VideoSourceProtocol.Http
                    http -> VideoSourceProtocol.Http
                    rtmp -> VideoSourceProtocol.Rtmp
                    file -> VideoSourceProtocol.File
                    else -> throw Exception("TkpdVideoPlayer: Only supported http, https, rtmp, and file format.")
                }
            }
        }
    }

}