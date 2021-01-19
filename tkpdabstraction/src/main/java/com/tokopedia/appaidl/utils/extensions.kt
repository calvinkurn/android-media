package com.tokopedia.appaidl.utils

import android.content.Context
import android.content.pm.PackageManager

fun Context.isInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}