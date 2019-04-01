package com.tokopedia.videorecorder.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

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

internal fun formatter(num: Long): String {
    return String.format("%02d", num)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}