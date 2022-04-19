package com.tokopedia.utils.view

import android.content.Context
import android.content.res.Configuration

/**
 * Created by Yehezkiel on 20/11/20
 */
object DarkModeUtil {

    @JvmStatic
    fun Context?.isDarkMode(): Boolean {
        if (this == null) return false
        val mode = this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

}