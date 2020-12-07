package com.tokopedia.play.util.video.buffer

import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
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
        private val playVideoManager: PlayVideoManager,
        private val dispatcher: CoroutineDispatcherProvider,
        private val scope: CoroutineScope
) {

    class Factory @Inject constructor (
            private val playVideoManager: PlayVideoManager,
            private val dispatcher: CoroutineDispatcherProvider
    ) {

        fun create(scope: CoroutineScope): PlayViewerVideoBufferGovernor {
            return PlayViewerVideoBufferGovernor(
                    playVideoManager = playVideoManager,
                    dispatcher = dispatcher,
                    scope = scope
            )
        }
    }

    fun startBufferGovernance() {
        scope.launch(dispatcher.immediate) {
            playVideoManager.getVideoStateFlow()
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
        stopPlayer()
        playVideoManager.resumeOrPlayPreviousVideo(true)
    }

    private fun stopPlayer() {
        playVideoManager.stop(resetState = false)
    }

    companion object {
        private const val MAX_BUFFERING_DURATION_IN_MS = 12 * 1000L
    }
}