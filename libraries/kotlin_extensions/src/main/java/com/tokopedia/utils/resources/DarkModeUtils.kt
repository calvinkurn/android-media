package com.tokopedia.utils.resources

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

fun Context.isDarkMode(): Boolean {
    return try {
        when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    } catch (ignored: Exception) {
        false
    }
}

fun isAppDarkMode(): Boolean {
    val currentNightMode = AppCompatDelegate.getDefaultNightMode()
    return currentNightMode == AppCompatDelegate.MODE_NIGHT_YES
}