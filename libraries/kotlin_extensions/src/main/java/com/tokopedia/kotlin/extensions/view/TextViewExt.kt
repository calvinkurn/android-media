package com.tokopedia.kotlin.extensions.view

import android.view.View
import android.widget.TextView

fun TextView.displayTextOrHide(text : String) {
    if (text.isNotEmpty()) {
        this.visibility = View.VISIBLE
        this.text = text
    } else {
        this.visibility = View.GONE
    }
}

fun TextView.setTextAndContentDescription(text: String?, contentDescriptionTemplate: Int) {
    this.text = text
    if (!text.isNullOrEmpty()) {
        val contentDescription =  this.context.getString(contentDescriptionTemplate)
        this.contentDescription = "$contentDescription $text"
    }
}