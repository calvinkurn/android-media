package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/01/20
 */
sealed class KeyboardState {

    abstract val isPreviousStateSame: Boolean

    data class Shown(val estimatedKeyboardHeight: Int, override val isPreviousStateSame: Boolean) : KeyboardState()
    data class Hidden(override val isPreviousStateSame: Boolean) : KeyboardState()

    val isShown: Boolean
        get() = this is Shown

    val isHidden: Boolean
        get() = this is Hidden
}