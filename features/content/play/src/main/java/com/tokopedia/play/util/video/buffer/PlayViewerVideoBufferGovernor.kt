package com.tokopedia.play.util.video.buffer

import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/09/20
 */
class PlayViewerVideoBufferGovernor (
        private val playVideoPlayer: PlayVideoWrapper,
        private val dispatcher: CoroutineDispatchers,
        private val scope: CoroutineScope
) {

    class Factory @Inject constructor (
            private val dispatcher: CoroutineDispatchers
    ) {

        fun create(
                playVideoPlayer: PlayVideoWrapper,
                scope: CoroutineScope): PlayViewerVideoBufferGovernor {
            return PlayViewerVideoBufferGovernor(
                    playVideoPlayer = playVideoPlayer,
                    dispatcher = dispatcher,
                    scope = scope
            )
        }
    }

    fun startBufferGovernance() {
        scope.launch(dispatcher.immediate) {
            playVideoPlayer.getVideoStateFlow()
                    .distinctUntilChanged()
                    .flowOn(dispatcher.io)
                    .collectLatest { state ->
                        if (state == PlayVideoState.Buffering) {
                            delay(MAX_BUFFERING_DURATION_IN_MS)
                            resetPlayer()
                        }
                    }
        }
    }

    private fun resetPlayer() {
        val videoUri = playVideoPlayer.currentUri

        if (playVideoPlayer.isVideoLive()) playVideoPlayer.release()
        else playVideoPlayer.stop(resetState = false)

        if (videoUri != null) playVideoPlayer.playUri(videoUri, autoPlay = true)
    }

    companion object {
        private const val MAX_BUFFERING_DURATION_IN_MS = 12 * 1000L
    }
}