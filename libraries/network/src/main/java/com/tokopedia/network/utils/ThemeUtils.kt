package com.tokopedia.network.utils

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by yovi.putra on 14/06/23"
 * Project name: android-tokopedia-core
 **/

object ThemeUtils {

    @JvmStatic
    fun getHeader(context: Context): String = try {
        val isDarkMode = isHomeAccountUserDarkModelEnable(context)

        if (isDarkMode) {
            "dark"
        } else {
            /**
             * When tokopedia has supported for color blind theme, please add this
             * Light -> default
             * Dark -> dark
             * TODO Deu & Prota -> prota
             * TODO Trita -> trita
             * ref: https://tokopedia.slack.com/archives/C0328MUFV6D/p1683094397706009?thread_ts=1683090166.400889&cid=C0328MUFV6D
             */
            "default"
        }
    } catch (ignored: Exception) {
        "default"
    }

    private fun isHomeAccountUserDarkModelEnable(context: Context): Boolean {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        return pref.getBoolean("KEY_DARK_MODE", false)
    }
}
