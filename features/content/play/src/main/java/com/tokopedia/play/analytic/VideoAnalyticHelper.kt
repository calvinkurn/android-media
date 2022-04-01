package com.tokopedia.play.analytic

import android.content.Context
import android.util.Log
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.util.logger.PlayLog
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import kotlin.math.abs

/**
 * Created by jegul on 13/05/20
 */
class VideoAnalyticHelper(
        private val context: Context,
        private val analytic: PlayAnalytic,
        private val log: PlayLog,
        private val channelData: PlayChannelData
) {

    @TrackingField
    private var bufferTrackingModel = BufferTrackingModel(
            isBuffering = false,
            bufferCount = 0,
            lastBufferMs = System.currentTimeMillis(),
            shouldTrackNext = false
    )

    @TrackingField
    private var watchDurationModel = WatchDurationModel(
            watchTime = null,
            cumulationDuration = 0L
    )

    fun onPause() {
        bufferTrackingModel = bufferTrackingModel.copy(
                isBuffering = false,
                bufferCount = if (bufferTrackingModel.isBuffering) bufferTrackingModel.bufferCount - 1 else bufferTrackingModel.bufferCount,
                shouldTrackNext = false
        )
    }

    fun onNewVideoState(state: PlayViewerVideoState) {
        handleBufferAnalytics(state)
        handleDurationAnalytics(state)
    }

    private fun handleBufferAnalytics(state: PlayViewerVideoState) {
        if (state is PlayViewerVideoState.Error) {
            sendErrorStateVideoAnalytic(state.error.message ?: context.getString(com.tokopedia.play_common.R.string.play_common_video_error_message))

        } else if (state is PlayViewerVideoState.Buffer && !bufferTrackingModel.isBuffering) {
            val nextBufferCount = if (bufferTrackingModel.shouldTrackNext) bufferTrackingModel.bufferCount + 1 else bufferTrackingModel.bufferCount

            bufferTrackingModel = BufferTrackingModel(
                    isBuffering = true,
                    bufferCount = nextBufferCount,
                    lastBufferMs = System.currentTimeMillis(),
                    shouldTrackNext = bufferTrackingModel.shouldTrackNext
            )

            val bufferEvent = PlayLiveRoomMetricsCommon.getBufferingEventData(bufferCount = bufferTrackingModel.bufferCount, timestamp = bufferTrackingModel.lastBufferMs)
            log.logBufferEvent(bufferingCount = bufferEvent.second, bufferingEvent = bufferEvent.first)
            log.sendAll(channelData.id, (channelData.videoMetaInfo.videoPlayer as PlayVideoPlayerUiModel.General).params.videoUrl)

        } else if ((state is PlayViewerVideoState.Play || state is PlayViewerVideoState.Pause) && bufferTrackingModel.isBuffering) {
            if (bufferTrackingModel.shouldTrackNext) sendVideoBufferingAnalytic()

            bufferTrackingModel = bufferTrackingModel.copy(
                    isBuffering = false,
                    shouldTrackNext = true
            )
        }
    }

    private fun handleDurationAnalytics(state: PlayViewerVideoState) {
        if (state is PlayViewerVideoState.Play) {
            if (watchDurationModel.watchTime == null) watchDurationModel = watchDurationModel.copy(
                    watchTime = System.currentTimeMillis()
            )
        } else {
            val watchTime = watchDurationModel.watchTime
            if (watchTime != null) watchDurationModel = watchDurationModel.copy(
                    watchTime = null,
                    cumulationDuration = watchDurationModel.cumulationDuration + abs(System.currentTimeMillis() - watchTime)
            )
        }

        log.logWatchingDuration(watchDurationModel.cumulationDuration.toString())
    }

    /**
     * Send Analytic
     */
    fun sendLeaveRoomAnalytic(channelId: String) {
        if (channelId != analytic.channelId) return

        val currentWatchDuration = watchDurationModel.watchTime?.let { abs(System.currentTimeMillis() - it) }.orZero()
        val totalDuration = watchDurationModel.cumulationDuration + currentWatchDuration
        analytic.clickLeaveRoom(totalDuration)
    }

    private fun sendVideoBufferingAnalytic() {
        analytic.trackVideoBuffering(
                bufferCount = bufferTrackingModel.bufferCount,
                bufferDurationInSecond = ((System.currentTimeMillis() - bufferTrackingModel.lastBufferMs) / 1000).toInt()
        )
    }

    private fun sendErrorStateVideoAnalytic(errorMessage: String) {
        analytic.trackVideoError(errorMessage)
    }
}