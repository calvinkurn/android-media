package com.tokopedia.videoplayer.state

import com.tokopedia.videoplayer.R

sealed class VideoSourceProtocol {

    object Http: VideoSourceProtocol()
    object Rtmp: VideoSourceProtocol()
    object File: VideoSourceProtocol()
    data class InvalidFormat(val message: Int): VideoSourceProtocol()

    companion object {
        const val http  = "http"
        const val https = "https"
        const val rtmp  = "rtmp"
        const val file  = "file"

        fun protocol(source: String): VideoSourceProtocol {
            if (!source.contains(":")) {
                return InvalidFormat(R.string.videoplayer_invalid_protocol_format)
            } else {
                val url = source.split(":").first()
                return when (url) {
                    https -> Http
                    http -> Http
                    rtmp -> Rtmp
                    file -> File
                    else -> InvalidFormat(R.string.videoplayer_invalid_protocol_type)
                }
            }
        }
    }

}