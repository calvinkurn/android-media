package com.tokopedia.play.view.type

/**
 * Created by jegul on 25/02/21
 */
sealed class PiPState {

    data class Requesting(val pipMode: PiPMode) : PiPState()
    data class InPiP(val pipMode: PiPMode) : PiPState()
    object Stop : PiPState()

    val isInPiP: Boolean
        get() = this is InPiP

    val mode: PiPMode?
        get() = when (this) {
            is Requesting -> pipMode
            is InPiP -> pipMode
            Stop -> null
        }
}