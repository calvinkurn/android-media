package com.tokopedia.play.util.video.state

/**
 * Created by jegul on 28/08/20
 */
sealed class PlayViewerVideoState {

    object Play : PlayViewerVideoState()
    object Pause : PlayViewerVideoState()
    object End : PlayViewerVideoState()
    object Waiting : PlayViewerVideoState()
    data class Buffer(val bufferSource: BufferSource) : PlayViewerVideoState()
    data class Error(val error: Throwable) : PlayViewerVideoState()
}

enum class BufferSource {

    Viewer,
    Broadcaster
}