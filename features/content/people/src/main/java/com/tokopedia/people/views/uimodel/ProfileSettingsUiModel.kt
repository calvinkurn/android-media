package com.tokopedia.people.views.uimodel

import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
data class ProfileSettingsUiModel(
    val settingID: String,
    val title: String,
    val isEnabled: Boolean
) {
    val icon: Int
        get() {
            return when (settingID) {
                SETTING_ID_REVIEW -> IconUnify.STAR
                else -> IconUnify.SETTING
            }
        }

    companion object {
        const val SETTING_ID_REVIEW = "1"

        val Empty: ProfileSettingsUiModel
            get() = ProfileSettingsUiModel(
                settingID = "",
                title = "",
                isEnabled = false,
            )
    }
}

fun List<ProfileSettingsUiModel>.getReviewSettings(): ProfileSettingsUiModel {
    return firstOrNull { it.settingID == ProfileSettingsUiModel.SETTING_ID_REVIEW } ?: ProfileSettingsUiModel.Empty
}
