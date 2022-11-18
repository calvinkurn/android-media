package com.tokopedia.kol.common.player

import com.tokopedia.kol.R

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
            return if (!source.contains(":")) {
                InvalidFormat(R.string.kol_invalid_protocol_format)
            } else {
                when (source.split(":").first()) {
                    https -> Http
                    http -> Http
                    rtmp -> Rtmp
                    file -> File
                    else -> InvalidFormat(R.string.kol_invalid_protocol_type)
                }
            }
        }
    }
}
