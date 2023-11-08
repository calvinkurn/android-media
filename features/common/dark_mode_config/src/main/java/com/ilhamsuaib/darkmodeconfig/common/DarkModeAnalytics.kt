package com.ilhamsuaib.darkmodeconfig.common

import com.ilhamsuaib.darkmodeconfig.model.UiMode
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

    fun eventClickThemeSetting(mode: UiMode) {
        val label: String = when (mode) {
            is UiMode.Light -> "light"
            is UiMode.Dark -> "dark"
            is UiMode.FollowSystemSetting -> "followSystemSetting"
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
}