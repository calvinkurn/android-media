package com.tokopedia.abstraction.common.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build

object DeviceChecker {
    private const val MINIMUM_DEVICE_MEMORY = 256
    private const val MINIMUM_DEVICE_PROCESSORS = 4

    fun isLowPerformingDevice(context: Context?): Boolean {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val runtime = Runtime.getRuntime()
        if (context == null || activityManager == null) {
            return false
        }

        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT ||
                activityManager.isLowRamDevice ||
                runtime.availableProcessors() < MINIMUM_DEVICE_PROCESSORS ||
                activityManager.memoryClass < MINIMUM_DEVICE_MEMORY
    }
}