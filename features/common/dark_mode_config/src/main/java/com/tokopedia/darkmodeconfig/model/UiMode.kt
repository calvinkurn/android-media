package com.tokopedia.darkmodeconfig.model

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.tokopedia.darkmodeconfig.R as darkmodeconfigR

/**
 * Created by @ilhamsuaib on 05/11/23.
 */

sealed class UiMode(
    val screenMode: Int,
    @StringRes val nameResId: Int,
    @StringRes val descriptionResId: Int,
    open val isSelected: Boolean
) {

    abstract fun copyModel(isSelected: Boolean): UiMode

    data class Light(override val isSelected: Boolean) : UiMode(
        AppCompatDelegate.MODE_NIGHT_NO,
        darkmodeconfigR.string.dmc_light_mode_name,
        darkmodeconfigR.string.dmc_light_mode_description,
        isSelected
    ) {
        override fun copyModel(isSelected: Boolean): UiMode {
            return this.copy(isSelected = isSelected)
        }
    }

    data class Dark(override val isSelected: Boolean) : UiMode(
        AppCompatDelegate.MODE_NIGHT_YES,
        darkmodeconfigR.string.dmc_dark_mode_name,
        darkmodeconfigR.string.dmc_dark_mode_description,
        isSelected
    ) {
        override fun copyModel(isSelected: Boolean): UiMode {
            return this.copy(isSelected = isSelected)
        }
    }

    data class FollowSystemSetting(override val isSelected: Boolean = false) : UiMode(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        darkmodeconfigR.string.dmc_follow_system_name,
        darkmodeconfigR.string.dmc_follow_system_description,
        isSelected
    ) {
        override fun copyModel(isSelected: Boolean): UiMode {
            return this.copy(isSelected = isSelected)
        }
    }

    companion object {
        fun getOptionList(screenMode: Int): List<UiMode> {
            return listOf(
                FollowSystemSetting(isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
                Light(isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_NO),
                Dark(isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_YES)
            )
        }
    }
}