package com.tokopedia.media.picker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.tokopedia.config.GlobalConfig
import com.tokopedia.picker.common.PickerParam

fun Context.goToSettings(): Intent {
    return Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.parse("package:$packageName")
        addCategory(Intent.CATEGORY_DEFAULT)
    }
}

fun exceptionHandler(invoke: () -> Unit) {
    try {
        invoke()
    } catch (e: Exception) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
    }
}

fun isOppoManufacturer(): Boolean {
    return Build.MANUFACTURER.contains("oppo", ignoreCase = true)
}

fun PickerParam.generateKey(): String {
    return this.pageSourceName() + this.subPageSourceName()
}
