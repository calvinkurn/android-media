package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/03/20
 */
sealed class BottomInsetsType {

    object Keyboard : BottomInsetsType()
    data class BottomSheet(val height: Int?) : BottomInsetsType()

    val isKeyboard: Boolean
        get() = this is Keyboard

    val isBottomSheet: Boolean
        get() = this is BottomSheet
}