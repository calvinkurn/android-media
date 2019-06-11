package com.tokopedia.videoplayer.utils

import android.util.Log
import java.net.URL

sealed class VideoSourceProtocol {

    object Http: VideoSourceProtocol()
    object Rtmp: VideoSourceProtocol()
    object File: VideoSourceProtocol()

    companion object {
        const val http = "http"
        const val rtmp = "rtmp"
        const val file = "file"

        fun protocol(source: String): VideoSourceProtocol {
            val url = URL(source)
            return when (url.protocol) {
                http -> {
                    Log.d("VideoDetailPlayer", http)
                    VideoSourceProtocol.Http
                }
                rtmp -> {
                    Log.d("VideoDetailPlayer", rtmp)
                    VideoSourceProtocol.Rtmp
                }
                file -> {
                    Log.d("VideoDetailPlayer", file)
                    VideoSourceProtocol.File
                }
                else -> VideoSourceProtocol.File
            }
        }
    }

}