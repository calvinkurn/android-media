package com.tokopedia.play.util.video.state

/**
 * Created by jegul on 28/08/20
 */
sealed class PlayViewerVideoState {

    object Play : PlayViewerVideoState()
    object Pause : PlayViewerVideoState()
    object End : PlayViewerVideoState()
    object Waiting : PlayViewerVideoState()
    object Unknown : PlayViewerVideoState()
    data class Buffer(val bufferSource: BufferSource) : PlayViewerVideoState()
    data class Error(val error: Throwable) : PlayViewerVideoState()
}

val PlayViewerVideoState.hasNoData: Boolean
    get() = this == PlayViewerVideoState.Waiting ||
            this == PlayViewerVideoState.Unknown ||
            this is PlayViewerVideoState.Buffer ||
            this is PlayViewerVideoState.Error

enum class BufferSource {

    Viewer,
    Broadcaster,
    Unknown
}