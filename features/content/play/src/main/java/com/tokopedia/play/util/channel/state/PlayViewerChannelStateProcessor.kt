package com.tokopedia.play.util.channel.state

import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/09/20
 */
class PlayViewerChannelStateProcessor constructor(
        private val playVideoPlayer: PlayVideoWrapper,
        private val exoPlaybackExceptionParser: ExoPlaybackExceptionParser,
        private val channelTypeSource: () -> PlayChannelType,
        private val videoPlayerSource: () -> PlayVideoPlayerUiModel,
        private val dispatcher: CoroutineDispatchers,
        private val scope: CoroutineScope
) {

    @PlayScope
    class Factory @Inject constructor(
            private val exoPlaybackExceptionParser: ExoPlaybackExceptionParser,
            private val dispatcher: CoroutineDispatchers
    ) {
        fun create(
                playVideoPlayer: PlayVideoWrapper,
                scope: CoroutineScope,
                channelTypeSource: () -> PlayChannelType,
                videoPlayerSource: () -> PlayVideoPlayerUiModel
        ): PlayViewerChannelStateProcessor {
            return PlayViewerChannelStateProcessor(
                    playVideoPlayer = playVideoPlayer,
                    exoPlaybackExceptionParser = exoPlaybackExceptionParser,
                    channelTypeSource = channelTypeSource,
                    videoPlayerSource = videoPlayerSource,
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
            playVideoPlayer.getVideoStateFlow()
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
        println("ChannelState Invalidate")
        broadcastState(shouldFreezeChannel())
    }

    private fun shouldFreezeChannel(): Boolean {
        val source = channelTypeSource()
        return when {
            videoPlayerSource().isYouTube -> mIsFreeze
            source.isLive -> {
                if (mIsFreeze) {
                    if (mIsEnded) true
                    else isErrorFromBroadcaster(mError)
                }
                else false
            }
            else -> mIsFreeze
        }
    }

    private fun handleState(state: PlayVideoState) {
        println("ChannelState State: $state")
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
        println("ChannelState Error: $error")
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