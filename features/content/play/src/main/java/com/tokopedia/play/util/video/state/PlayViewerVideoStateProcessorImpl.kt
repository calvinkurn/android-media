package com.tokopedia.play.util.video.state

import com.google.android.exoplayer2.ExoPlaybackException
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import javax.inject.Inject

/**
 * Created by jegul on 28/08/20
 */
class PlayViewerVideoStateProcessorImpl @Inject constructor(
        playVideoManager: PlayVideoManager,
        private val exoPlaybackExceptionParser: ExoPlaybackExceptionParser
) : PlayViewerVideoStateProcessor {

    private var error: Throwable? = null
    private var isFirstTime: Boolean = true

    private val goodStates = arrayOf(PlayVideoState.Playing, PlayVideoState.Pause, PlayVideoState.Ended)

    init {
        playVideoManager.addListener(object : PlayVideoManager.Listener {
            override fun onPlayerStateChanged(state: PlayVideoState) {
                if (state in goodStates) isFirstTime = false

                when (state) {
                    is PlayVideoState.Error -> error = state.error
                    PlayVideoState.Buffering -> {
                        val bufferSource = getBufferSourceFromError(error)
                        broadcastState(
                                if (isFirstTime && bufferSource == BufferSource.Broadcaster) PlayViewerVideoState.Waiting
                                else PlayViewerVideoState.Buffer(bufferSource)
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
        })
    }

    private val mListeners = mutableListOf<PlayViewerVideoStateListener>()

    override fun addStateListener(listener: PlayViewerVideoStateListener) {
        mListeners.add(listener)
    }

    override fun removeStateListener(listener: PlayViewerVideoStateListener) {
        mListeners.remove(listener)
    }

    private fun getBufferSourceFromError(error: Throwable?): BufferSource {
        if (error == null || error !is ExoPlaybackException) return BufferSource.Viewer
        val parsedException = exoPlaybackExceptionParser.parse(error)

        return if (parsedException.isInvalidResponseCodeException) BufferSource.Broadcaster
        else BufferSource.Viewer
    }

    private fun broadcastState(state: PlayViewerVideoState) {
        mListeners.forEach { it.onStateChanged(state) }
    }
}