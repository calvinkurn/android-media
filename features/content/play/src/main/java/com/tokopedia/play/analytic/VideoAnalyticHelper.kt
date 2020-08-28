package com.tokopedia.play.analytic

import android.content.Context
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.ERR_STATE_VIDEO
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.state.PlayVideoState
import kotlin.math.abs

/**
 * Created by jegul on 13/05/20
 */
class VideoAnalyticHelper(
        private val context: Context,
        private val channelId: String
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

    fun onNewVideoState(userId: String, channelType: PlayChannelType, state: PlayViewerVideoState) {
        handleBufferAnalytics(userId, channelType, state)
        handleDurationAnalytics(state)
    }

    private fun handleBufferAnalytics(userId: String, channelType: PlayChannelType, state: PlayViewerVideoState) {
        if (state is PlayViewerVideoState.Error) {
            sendErrorStateVideoAnalytic(channelType, state.error.message ?: context.getString(com.tokopedia.play_common.R.string.play_common_video_error_message))

        } else if (state is PlayViewerVideoState.Buffer && !bufferTrackingModel.isBuffering) {
            val nextBufferCount = if (bufferTrackingModel.shouldTrackNext) bufferTrackingModel.bufferCount + 1 else bufferTrackingModel.bufferCount

            bufferTrackingModel = BufferTrackingModel(
                    isBuffering = true,
                    bufferCount = nextBufferCount,
                    lastBufferMs = System.currentTimeMillis(),
                    shouldTrackNext = bufferTrackingModel.shouldTrackNext
            )

        } else if ((state is PlayViewerVideoState.Play || state is PlayViewerVideoState.Pause) && bufferTrackingModel.isBuffering) {
            if (bufferTrackingModel.shouldTrackNext) sendVideoBufferingAnalytic(userId, channelType)

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
    }

    /**
     * Send Analytic
     */
    fun sendLeaveRoomAnalytic(channelType: PlayChannelType) {
        val currentWatchDuration = watchDurationModel.watchTime?.let { abs(System.currentTimeMillis() - it) }.orZero()
        val totalDuration = watchDurationModel.cumulationDuration + currentWatchDuration
        PlayAnalytics.clickLeaveRoom(channelId, totalDuration, channelType)
    }

    private fun sendVideoBufferingAnalytic(userId: String, channelType: PlayChannelType) {
        PlayAnalytics.trackVideoBuffering(
                bufferCount = bufferTrackingModel.bufferCount,
                bufferDurationInSecond = ((System.currentTimeMillis() - bufferTrackingModel.lastBufferMs) / 1000).toInt(),
                userId = userId,
                channelId = channelId,
                channelType = channelType
        )
    }

    private fun sendErrorStateVideoAnalytic(channelType: PlayChannelType, errorMessage: String) {
        PlayAnalytics.errorState(channelId,
                "$ERR_STATE_VIDEO: $errorMessage",
                channelType)
    }
}