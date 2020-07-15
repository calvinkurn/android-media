package com.tokopedia.play_common.state

/**
 * Created by jegul on 10/12/19
 */
sealed class PlayVideoState {

    object Buffering : PlayVideoState()
    object Playing : PlayVideoState()
    object Pause : PlayVideoState()
    object NoMedia : PlayVideoState()
    object Ended : PlayVideoState()
    data class Error(val error: Throwable) : PlayVideoState()
}