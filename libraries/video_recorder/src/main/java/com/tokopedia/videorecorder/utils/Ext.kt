package com.tokopedia.videorecorder.utils

import android.view.View

/**
 * Created by isfaaghyth on 01/03/19.
 * github: @isfaaghyth
 */

internal fun View.show() {
    visibility = View.VISIBLE
}

internal fun View.hide() {
    visibility = View.GONE
}

internal fun View.visible(isTrue: Boolean, it: () -> Unit) {
    if (isTrue) {
        visibility = View.VISIBLE
        it()
    } else {
        visibility = View.GONE
    }
}

infix fun <T>Boolean.then(action : () -> T): T? {
    return if (this)
        action.invoke()
    else null
}

infix fun <T>T?.elze(action: () -> T): T {
    return if (this == null)
        action.invoke()
    else this
}
