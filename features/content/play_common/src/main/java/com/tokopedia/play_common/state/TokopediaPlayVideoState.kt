package com.tokopedia.play_common.state

/**
 * Created by jegul on 10/12/19
 */
sealed class TokopediaPlayVideoState {

    object Buffering : TokopediaPlayVideoState()
    object Ready : TokopediaPlayVideoState()
    object NoMedia : TokopediaPlayVideoState()
    object Ended : TokopediaPlayVideoState()
    data class Error(val error: Throwable) : TokopediaPlayVideoState()
}