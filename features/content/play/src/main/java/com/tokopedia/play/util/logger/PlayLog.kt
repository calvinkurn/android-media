package com.tokopedia.play.util.logger

import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon


/**
 * @author by astidhiyaa on 29/03/22
 */
interface PlayLog {
    fun logTimeToFirstByte(
        timeToFirstByte: Long
    )

    fun logDownloadSpeed(
        downloadSpeed: Float
    )

    fun logBufferEvent(
        bufferingEvent: PlayLiveRoomMetricsCommon.BufferEvent,
        bufferingCount: Int
    )

    fun logWatchingDuration(
        watchingTime: String
    )

    fun sendAll(channelId: String, videoPlayer: PlayVideoPlayerUiModel)
}
