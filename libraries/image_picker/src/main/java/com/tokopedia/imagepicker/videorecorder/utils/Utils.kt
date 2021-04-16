package com.tokopedia.imagepicker.videorecorder.utils

import android.content.Context
import com.google.android.material.tabs.TabLayout
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.config.GlobalConfig

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */

fun exceptionHandler(func: () -> Unit) {
    try {
        func()
    } catch (e: Exception) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
    }
}

internal fun formatter(num: Long): String {
    return String.format("%02d", num)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun TabLayout.tabClickable(enabled: Boolean) {
    val parentView = this.getChildAt(0) as ViewGroup?
    if (parentView != null) {
        for (index in 0 until parentView.childCount) {
            val tabView = parentView.getChildAt(index)
            if (tabView != null) {
                tabView.isEnabled = enabled
            }
        }
    }
}