package com.tokopedia.navigation_common.util

import android.content.Context
import android.content.res.Configuration

val Context.inDarkMode: Context
    get() {
        val newConfig = Configuration(resources.configuration).apply {
            uiMode = Configuration.UI_MODE_NIGHT_YES or
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
        }
        return createConfigurationContext(newConfig)
    }
