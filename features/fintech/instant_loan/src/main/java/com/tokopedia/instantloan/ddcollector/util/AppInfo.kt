package com.tokopedia.instantloan.ddcollector.util

import android.content.Context

/**
 * Utility class for fetching the application information
 */
object AppInfo {

    fun getPackageName(context: Context): String {
        return context.packageName
    }

    fun getDefaultAcceptLanguage(context: Context): String? =
            context.resources.configuration?.locale?.toString()?.replace('_', '-')
}
