package com.tokopedia.play.view.type

/**
 * Created by jegul on 02/01/20
 */
enum class KeyboardState {

    Shown,
    Hidden;

    val isShown: Boolean
        get() = this == Shown
}