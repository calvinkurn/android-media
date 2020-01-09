package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/01/20
 */
sealed class KeyboardState {

    data class Shown(val estimatedKeyboardHeight: Int) : KeyboardState()
    object Hidden : KeyboardState()

    val isShown: Boolean
        get() = this is Shown
}