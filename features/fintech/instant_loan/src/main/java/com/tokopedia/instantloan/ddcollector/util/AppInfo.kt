package com.tokopedia.instantloan.ddcollector.util

import android.content.Context

/**
 * Utility class for fetching the application information
 */
object AppInfo {

    fun getDefaultAcceptLanguage(context: Context): String? =
            context.resources.configuration?.locale?.toString()?.replace('_', '-')
}
