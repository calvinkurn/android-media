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
            val url = source.split(":").first()
            return when (url) {
                https -> VideoSourceProtocol.Http
                http -> VideoSourceProtocol.Http
                rtmp -> VideoSourceProtocol.Rtmp
                file -> VideoSourceProtocol.File
                else -> throw Exception("Only supported http, rtmp, and file format.")
            }
        }
    }

}