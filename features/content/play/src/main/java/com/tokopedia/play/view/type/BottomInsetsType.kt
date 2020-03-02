package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/03/20
 */
enum class BottomInsetsType {

    Keyboard,
    BottomSheet;

    val isKeyboard: Boolean
        get() = this == Keyboard

    val isBottomSheet: Boolean
        get() = this == BottomSheet
}