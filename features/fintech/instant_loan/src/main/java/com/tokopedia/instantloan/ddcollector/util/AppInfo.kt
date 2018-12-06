package com.tokopedia.instantloan.ddcollector.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log

import java.util.Locale

/**
 * Utility class for fetching the application information
 */
object AppInfo {

    fun getPackageName(context: Context): String {
        return context.packageName
    }

    fun getDefaultAcceptLanguage(context: Context): String? {
        val configuration = context.resources.configuration ?: return null

        val locale = configuration.locale ?: return null

        return locale.toString().replace('_', '-')
    }
}
