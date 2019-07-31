package com.tokopedia.videoplayer.utils

import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by isfaaghyth on 29/04/19.
 * github: @isfaaghyth
 */

fun sendViewToBack(child: View) {
    val parent = child.parent as ViewGroup
    parent.removeView(child)
    parent.addView(child, 0)
}

fun Fragment.showToast(message: Int) {
    showToast(context?.getString(message))
}

fun Fragment.showToast(message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}