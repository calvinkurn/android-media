package com.tokopedia.notifications.utils

import android.content.Context
import android.content.pm.PackageManager

fun <T> ArrayList<T>.onlyOne(): Boolean {
    if (size == 1) return true
    return false
}

fun Context.isInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun <T, U> List<T>.intersect(
        uList: List<U>,
        filterPredicate : (T, U) -> Boolean
) = filterNot { m -> uList.any { filterPredicate(m, it)} }
