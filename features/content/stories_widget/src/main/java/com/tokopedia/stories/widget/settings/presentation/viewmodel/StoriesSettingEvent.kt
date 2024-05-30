package com.tokopedia.stories.widget.settings.presentation.viewmodel

import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt

/**
 * @author by astidhiyaa on 4/19/24
 */
sealed interface StoriesSettingEvent {
    data class ShowSuccessToaster(
        val message: String? = null
    ) : StoriesSettingEvent

    data class ShowErrorToaster(
        val message: Throwable,
        val onClick: () -> Unit
    ) : StoriesSettingEvent

    data class ClickTrack(val option: StoriesSettingOpt) : StoriesSettingEvent
    data class Navigate(val appLink: String) : StoriesSettingEvent
}

sealed class StoriesSettingsAction {
    object FetchPageInfo : StoriesSettingsAction()
    data class SelectOption(val option: StoriesSettingOpt) : StoriesSettingsAction()
    data class Navigate(val appLink: String) : StoriesSettingsAction()
    object ShowCoolingDown: StoriesSettingsAction()
}
