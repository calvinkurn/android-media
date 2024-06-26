package com.tokopedia.test.application.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build


/**
 * Created by Yehezkiel on 2019-12-10.
 */
object DevicePerformanceInfo {

    private const val MINIMUM_DEVICE_MEMORY = 256
    private const val MINIMUM_DEVICE_PROCESSORS = 4

    fun isLow(context: Context?): Boolean {
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

    fun getDeviceDpi(context: Context?): String {
        return context?.applicationContext?.resources?.displayMetrics?.density?.toString()?:""
    }
}