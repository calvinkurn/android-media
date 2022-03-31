package com.tokopedia.play.util.logger


/**
 * @author by astidhiyaa on 29/03/22
 */
interface PlayLog {
    fun logTimeToFirstByte(
        timeToFirstByte: String
    )

    fun logDownloadSpeed(
        downloadSpeed: String
    )

    fun logBufferEvent(
        bufferingEvent: List<Pair<String, String>>,
        bufferingCount: Int
    )

    fun lofWatchingDuration(
        watchingTime: String
    )

    fun sendAll(channelId: String, streamingUrl: String)
}
