package com.tokopedia.people.views.uimodel

import androidx.annotation.StringRes
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.people.R

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
data class ProfileSettingsUiModel(
    val key: String,
    val icon: Int,
    @StringRes val text: Int,
    val isEnabled: Boolean
) {
    companion object {
        const val KEY_REVIEWS = "reviews"

        fun createReview(isEnabled: Boolean): ProfileSettingsUiModel {
            return ProfileSettingsUiModel(
                key = KEY_REVIEWS,
                icon = IconUnify.STAR,
                text = R.string.up_profile_settings_review_text,
                isEnabled = isEnabled,
            )
        }
    }
}
