package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

fun TextView.setTextAndCheckShow(text: String?) {
    if (text.isNullOrEmpty()) {
        gone()
    } else {
        setText(text)
        visible()
    }
}

fun ViewGroup.inflateLayout(layoutId: Int, isAttached: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, isAttached)
}

fun Activity.createDefaultProgressDialog(loadingMessage:String?,
                                         cancelable:Boolean = true,
                                         onCancelClicked: (() -> Unit)?) : ProgressDialog{
    return ProgressDialog(this).apply {
        setMessage(loadingMessage)
        setCancelable(cancelable)
        setOnCancelListener {
            onCancelClicked?.invoke()
            dismiss()
        }
    }
}