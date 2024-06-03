package com.tokopedia.stories.widget.settings.tracking

import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt

/**
 * @author by astidhiyaa on 4/19/24
 */
interface StoriesSettingsTracking {
    fun openScreen()

    fun clickToggle(option: StoriesSettingOpt)

    fun clickCheck(option: StoriesSettingOpt)
}
