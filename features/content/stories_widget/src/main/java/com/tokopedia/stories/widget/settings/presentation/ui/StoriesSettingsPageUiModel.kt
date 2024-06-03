package com.tokopedia.stories.widget.settings.presentation.ui

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.content.common.types.ResultState

/**
 * @author by astidhiyaa on 5/22/24
 */
data class StoriesSettingOpt(
    val text: String,
    val optionType: String,
    val isSelected: Boolean,
    val lastClicked: Long = 0,
)

data class StoriesSettingConfig(
    val articleCopy: String,
    val articleAppLink: String,
    val articleWebLink: String,
    val isEligible: Boolean
)

data class StoriesSettingsPageUiModel(
    val options: List<StoriesSettingOpt>, val config: StoriesSettingConfig, val state: ResultState
) {
    companion object {
        val Empty
            get() = StoriesSettingsPageUiModel(
                options = emptyList(),
                config = StoriesSettingConfig("", "", "", false),
                state = ResultState.Loading
            )
    }
}

data class CoolDownException(override val message: String = "Tunggu 5 detik dulu ya"): MessageErrorException(message)
