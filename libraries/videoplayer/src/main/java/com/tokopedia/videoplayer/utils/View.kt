package com.tokopedia.videoplayer.utils

import android.view.View
import android.view.ViewGroup

/**
 * Created by isfaaghyth on 29/04/19.
 * github: @isfaaghyth
 */

fun sendViewToBack(child: View) {
    val parent = child.parent as ViewGroup
    parent.removeView(child)
    parent.addView(child, 0)
}