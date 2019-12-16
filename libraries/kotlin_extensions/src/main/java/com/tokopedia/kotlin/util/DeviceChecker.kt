package com.tokopedia.kotlin.util

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

    fun getAvailableProcessor(context: Context?): String {
        val runtime = Runtime.getRuntime()
        if (context == null ) return ""
        return runtime.availableProcessors().toString()
    }

    fun getDeviceMemoryClassCapacity(context: Context?): String {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
                ?: return ""
        return activityManager.memoryClass.toString()
    }
}