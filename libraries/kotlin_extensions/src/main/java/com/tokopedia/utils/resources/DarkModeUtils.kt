package com.tokopedia.utils.resources

import android.content.Context
import android.content.res.Configuration

object DarkModeUtils {
    fun isDarkMode(context: Context): Boolean {
        return try {
            when (context.resources.configuration.uiMode and
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
}