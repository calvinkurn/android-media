package com.tokopedia.videorecorder.utils

import android.util.Log

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */

fun exceptionHandler(func: () -> Unit) {
    try {
        func()
    } catch (e: Exception) {
        //no-op
        Log.d("TKPDVideo", e.message)
    }
}