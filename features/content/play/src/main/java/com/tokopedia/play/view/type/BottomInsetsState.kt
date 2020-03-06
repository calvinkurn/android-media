package com.tokopedia.play.view.type

/**
 * Created by jegul on 04/03/20
 */
/**
 * For Keyboard : The keyboard can't be controlled and will show/hide first, the state will be broadcasted thereafter
 * For Others: The action itself is the one that is broadcasted instead of the state
 */
sealed class BottomInsetsState {

    abstract var isPreviousStateSame: Boolean
    abstract val type: BottomInsetsType

    data class Shown(override val type: BottomInsetsType, val estimatedInsetsHeight: Int, override var isPreviousStateSame: Boolean) : BottomInsetsState()
    data class Hidden(override val type: BottomInsetsType, override var isPreviousStateSame: Boolean) : BottomInsetsState()

    val isShown: Boolean
        get() = this is Shown

    val isHidden: Boolean
        get() = this is Hidden
}