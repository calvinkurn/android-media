package com.tokopedia.videoplayer.utils

import android.util.Log

sealed class VideoSourceProtocol {

    object Http: VideoSourceProtocol()
    object Rtmp: VideoSourceProtocol()
    object File: VideoSourceProtocol()

    companion object {
        const val http = "http"
        const val rtmp = "rtmp"
        const val file = "file"

        fun protocol(source: String): VideoSourceProtocol {
            val url = source.split(":").first()
            return when (url) {
                http -> VideoSourceProtocol.Http
                rtmp -> VideoSourceProtocol.Rtmp
                file -> VideoSourceProtocol.File
                else -> VideoSourceProtocol.File
            }
        }
    }

}