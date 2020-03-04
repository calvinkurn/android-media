package com.tokopedia.play.view.type

/**
 * Created by jegul on 04/03/20
 */
sealed class BottomInsetsState {

    abstract val isPreviousStateSame: Boolean
    abstract val type: BottomInsetsType

    data class Shown(override val type: BottomInsetsType, val estimatedInsetsHeight: Int, override val isPreviousStateSame: Boolean) : BottomInsetsState()
    data class Hidden(override val type: BottomInsetsType, override val isPreviousStateSame: Boolean) : BottomInsetsState()

    val isShown: Boolean
        get() = this is Shown

    val isHidden: Boolean
        get() = this is Hidden
}