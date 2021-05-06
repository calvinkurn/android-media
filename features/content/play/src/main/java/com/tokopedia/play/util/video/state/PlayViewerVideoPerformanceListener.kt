package com.tokopedia.play.util.video.state

/**
 * Created by mzennis on 09/03/21.
 */
interface PlayViewerVideoPerformanceListener : PlayViewerVideoStateListener {

    override fun onStateChanged(state: PlayViewerVideoState) {
        if (state is PlayViewerVideoState.Error) onError()
        else if (state is PlayViewerVideoState.Play) onPlaying()
    }

    fun onPlaying()

    fun onError()
}