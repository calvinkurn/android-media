package com.tokopedia.stories.widget.settings.data.repository

import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.stories.widget.settings.presentation.StoriesSettingsEntryPoint
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsPageUiModel

/**
 * @author by astidhiyaa on 3/26/24
 */
interface StoriesSettingsRepository {
    suspend fun getOptions() : StoriesSettingsPageUiModel
    suspend fun updateOption(option: StoriesSettingOpt) : Boolean
}
