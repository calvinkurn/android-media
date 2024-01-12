package com.tokopedia.darkmodeconfig.common

import com.tokopedia.darkmodeconfig.model.UiMode

/**
 * Created by @ilhamsuaib on 20/11/23.
 */
object Utils {

    fun getIsDarkModeStatus(mode: UiMode, isDarkModeOs: Boolean): Boolean {
        return when (mode) {
            is UiMode.Light -> false
            is UiMode.Dark -> true
            is UiMode.FollowSystemSetting -> isDarkModeOs
        }
    }
}