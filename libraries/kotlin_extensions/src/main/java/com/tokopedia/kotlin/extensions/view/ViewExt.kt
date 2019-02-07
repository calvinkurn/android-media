package com.tokopedia.kotlin.extensions.view

import android.view.View
import android.widget.TextView

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

val View.isVisible: Boolean
        get() = visibility == View.VISIBLE

fun TextView.setTextAndCheckShow(text:String?) {
    if (text.isNullOrEmpty()) {
        gone()
    } else {
        setText(text)
        visible()
    }
}