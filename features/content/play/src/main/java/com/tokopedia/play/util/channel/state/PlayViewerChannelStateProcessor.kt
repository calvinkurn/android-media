package com.tokopedia.play.util.channel.state

import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/09/20
 */
class PlayViewerChannelStateProcessor constructor(
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
        fun create(scope: CoroutineScope, channelTypeSource: () -> PlayChannelType): PlayViewerChannelStateProcessor {
            return PlayViewerChannelStateProcessor(
                    playVideoManager = playVideoManager,
                    exoPlaybackExceptionParser = exoPlaybackExceptionParser,
                    channelTypeSource = channelTypeSource,
                    dispatcher = dispatcher,
                    scope = scope
            )
        }
    }

    private var mIsFreeze: Boolean = false
    private var mError: Throwable? = null
    private var mIsEnded: Boolean = false

    private val mListeners = mutableListOf<PlayViewerChannelStateListener>()

    init {
        scope.launch(dispatcher.immediate) {
            playVideoManager.getVideoStateFlow()
                    .flowOn(dispatcher.io)
                    .collectLatest { handleState(it) }
        }
    }

    fun setIsFreeze(isFreeze: Boolean) {
        mIsFreeze = isFreeze
        invalidateState()
    }

    fun addStateListener(listener: PlayViewerChannelStateListener) {
        mListeners.add(listener)
    }

    fun removeStateListener(listener: PlayViewerChannelStateListener) {
        mListeners.remove(listener)
    }

    private fun invalidateState() {
        broadcastState(shouldFreezeChannel())
    }

    private fun shouldFreezeChannel(): Boolean {
        val source = channelTypeSource()
        return when {
            source.isLive -> {
                if (mIsFreeze) {
                    if (mIsEnded) true
                    else isErrorFromBroadcaster(mError)
                }
                else false
            }
            else -> false
        }
    }

    private fun handleState(state: PlayVideoState) {
        when (state) {
            is PlayVideoState.Error -> mError = state.error
            PlayVideoState.Buffering -> invalidateState()
            PlayVideoState.Playing -> mError = null
            PlayVideoState.Pause -> mError = null
            PlayVideoState.Ended -> {
                mError = null
                mIsEnded = true

                invalidateState()
            }
        }
    }

    private fun isErrorFromBroadcaster(error: Throwable?): Boolean {
        if (error == null || error !is ExoPlaybackException) return false
        val parsedException = exoPlaybackExceptionParser.parse(error)

        return parsedException.isInvalidResponseCodeException
    }

    private fun broadcastState(shouldFreeze: Boolean) {
        mListeners.forEach {
            it.onChannelFreezeStateChanged(shouldFreeze)
        }
    }
}