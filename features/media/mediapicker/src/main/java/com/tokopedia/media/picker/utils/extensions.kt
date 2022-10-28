package com.tokopedia.media.picker.utils

import com.tokopedia.config.GlobalConfig

fun exceptionHandler(invoke: () -> Unit) {
    try {
        invoke()
    } catch (e: Exception) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
    }
}