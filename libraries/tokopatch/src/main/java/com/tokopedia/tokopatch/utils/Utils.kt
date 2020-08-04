package com.tokopedia.tokopatch.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build


/**
 * Author errysuprayogi on 17,June,2020
 */
object Utils {

    @JvmStatic
    fun isDebuggable(context: Context): Boolean =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) !== 0

    @JvmStatic
    fun versionName(context: Context): String =
            context.packageManager.getPackageInfo(context.packageName, 0).versionName

    @JvmStatic
    fun versionCode(context: Context): String =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode.toString()
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toString()
            }

    @JvmStatic
    fun packageName(context: Context): String = context.packageName

}