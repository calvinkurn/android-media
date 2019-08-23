package com.tokopedia.videoplayer.utils

import android.content.Context
import com.tokopedia.videoplayer.R

sealed class VideoSourceProtocol {

    object Http: VideoSourceProtocol()
    object Rtmp: VideoSourceProtocol()
    object File: VideoSourceProtocol()

    companion object {
        const val http = "http"
        const val https = "https"
        const val rtmp = "rtmp"
        const val file = "file"

        fun protocol(context: Context?, source: String): VideoSourceProtocol {
            if (!source.contains(":")) {
                throw Exception(context?.getString(R.string.videoplayer_invalid_protocol_format))
            } else {
                val url = source.split(":").first()
                return when (url) {
                    https -> VideoSourceProtocol.Http
                    http -> VideoSourceProtocol.Http
                    rtmp -> VideoSourceProtocol.Rtmp
                    file -> VideoSourceProtocol.File
                    else -> throw Exception(context?.getString(R.string.videoplayer_invalid_protocol_type))
                }
            }
        }
    }

}