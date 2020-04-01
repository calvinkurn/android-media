package com.tokopedia.play_common.state

/**
 * Created by jegul on 10/12/19
 */
sealed class TokopediaPlayVideoState {

    object Buffering : TokopediaPlayVideoState()
    object Playing : TokopediaPlayVideoState()
    object Pause : TokopediaPlayVideoState()
    object NoMedia : TokopediaPlayVideoState()
    object Ended : TokopediaPlayVideoState()
    object NotConfigured : TokopediaPlayVideoState()
    data class Error(val error: Throwable) : TokopediaPlayVideoState()
}