package com.tokopedia.play.util.video.state

import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by jegul on 28/08/20
 */
class PlayViewerVideoStateProcessor(
        private val playVideoManager: PlayVideoManager,
        private val exoPlaybackExceptionParser: ExoPlaybackExceptionParser,
        private val channelTypeSource: () -> PlayChannelType,
        private val dispatcher: CoroutineDispatcherProvider,
        private val scope: CoroutineScope
) {

    @PlayScope
    class Factory @Inject constructor(
            private val playVideoManager: PlayVideoManager,
            private val exoPlaybackExceptionParser: ExoPlaybackExceptionParser,
            private val dispatcher: CoroutineDispatcherProvider
    ) {
        fun create(scope: CoroutineScope, channelTypeSource: () -> PlayChannelType): PlayViewerVideoStateProcessor {
            return PlayViewerVideoStateProcessor(
                    playVideoManager = playVideoManager,
                    exoPlaybackExceptionParser = exoPlaybackExceptionParser,
                    channelTypeSource = channelTypeSource,
                    dispatcher = dispatcher,
                    scope = scope
            )
        }
    }

    private var error: Throwable? = null
    private var isFirstTime: Boolean = true

    private val goodStates = arrayOf(PlayVideoState.Playing, PlayVideoState.Pause, PlayVideoState.Ended)

    init {
        scope.launch(dispatcher.immediate) {
            playVideoManager.getVideoStateFlow()
                    .flowOn(dispatcher.immediate)
                    .collectLatest { state ->
                        if (state in goodStates) isFirstTime = false

                        when (state) {
                            is PlayVideoState.Error -> {
                                error = state.error
                                broadcastState(PlayViewerVideoState.Error(state.error))
                            }
                            PlayVideoState.Buffering -> {
                                if (error == null) broadcastState(PlayViewerVideoState.Buffer(BufferSource.Unknown))

                                val bufferSource = getBufferSourceFromError(error)
                                val isLive = channelTypeSource().isLive
                                broadcastState(
                                        if (isWaitingState(bufferSource, isLive)) {
                                            yield()
                                            PlayViewerVideoState.Waiting
                                        }
                                        else PlayViewerVideoState.Buffer(
                                                if (isLive) bufferSource
                                                else BufferSource.Viewer
                                        )
                                )
                            }
                            PlayVideoState.Playing -> {
                                error = null
                                broadcastState(PlayViewerVideoState.Play)
                            }
                            PlayVideoState.Pause -> {
                                error = null
                                broadcastState(PlayViewerVideoState.Pause)
                            }
                            PlayVideoState.Ended -> {
                                error = null
                                broadcastState(PlayViewerVideoState.End)
                            }
                        }
                    }
        }
    }

    private val mListeners = mutableListOf<PlayViewerVideoStateListener>()

    fun addStateListener(listener: PlayViewerVideoStateListener) {
        mListeners.add(listener)
    }

    fun removeStateListener(listener: PlayViewerVideoStateListener) {
        mListeners.remove(listener)
    }

    private fun getBufferSourceFromError(error: Throwable?): BufferSource {
        if (error == null || error !is ExoPlaybackException) return BufferSource.Unknown
        val parsedException = exoPlaybackExceptionParser.parse(error)

        return if (parsedException.isInvalidResponseCodeException) BufferSource.Broadcaster
        else BufferSource.Viewer
    }

    private fun broadcastState(state: PlayViewerVideoState) {
        mListeners.forEach { it.onStateChanged(state) }
    }

    private suspend fun isWaitingState(bufferSource: BufferSource, isLive: Boolean): Boolean = withContext(dispatcher.io) {
        return@withContext when (bufferSource) {
            BufferSource.Broadcaster -> {
                isFirstTime && isLive
            }
            BufferSource.Unknown -> {
                delay(BUFFER_TO_WAITING_STATE_MAX_DURATION)
                isFirstTime && isLive
            }
            else -> false
        }
    }

    companion object {
        private const val BUFFER_TO_WAITING_STATE_MAX_DURATION = 4000L
    }
}