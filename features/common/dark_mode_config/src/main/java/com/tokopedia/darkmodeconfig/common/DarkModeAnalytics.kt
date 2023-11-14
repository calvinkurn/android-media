package com.tokopedia.darkmodeconfig.common

import com.tokopedia.darkmodeconfig.model.UiMode
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

/**
 * Created by @ilhamsuaib on 08/11/23.
 */

internal object DarkModeAnalytics {

    private const val EVENT_CLICK_SETTING = "clickSetting"
    private const val CATEGORY_SETTING_PAGE = "setting page"
    private const val ACTION_SAVE_THEME_SELECTION = "click simpan on theme selection"

    private const val LIGHT = "light"
    private const val DARK = "dark"
    private const val FOLLOW_SYSTEM_SETTING = "followSystemSetting - %s"

    fun eventClickThemeSetting(mode: UiMode, isDarkModeOS: Boolean) {
        val label: String = when (mode) {
            is UiMode.Light -> LIGHT
            is UiMode.Dark -> DARK
            is UiMode.FollowSystemSetting -> getFollowSystemSettingLabel(isDarkModeOS)
        }
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
            TrackAppUtils.gtmData(
                EVENT_CLICK_SETTING,
                CATEGORY_SETTING_PAGE,
                ACTION_SAVE_THEME_SELECTION,
                label
            )
        )
    }

    private fun getFollowSystemSettingLabel(isDarkModeOS: Boolean): String {
        val label = if (isDarkModeOS) DARK else LIGHT
        return String.format(FOLLOW_SYSTEM_SETTING, label)
    }
}