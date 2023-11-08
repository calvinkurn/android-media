package com.ilhamsuaib.darkmodeconfig.model

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
//noinspection MissingResourceImportAlias
import com.ilhamsuaib.darkmodeconfig.R

/**
 * Created by @ilhamsuaib on 05/11/23.
 */

internal sealed class UiMode(
    val screenMode: Int,
    @StringRes val nameResId: Int,
    @StringRes val descriptionResId: Int,
    open val isSelected: Boolean
) {

    abstract fun copyModel(isSelected: Boolean): UiMode

    data class Light(override val isSelected: Boolean) : UiMode(
        AppCompatDelegate.MODE_NIGHT_NO,
        R.string.dmc_light_mode_name,
        R.string.dmc_light_mode_description,
        isSelected
    ) {
        override fun copyModel(isSelected: Boolean): UiMode {
            return this.copy(isSelected = isSelected)
        }
    }

    data class Dark(override val isSelected: Boolean) : UiMode(
        AppCompatDelegate.MODE_NIGHT_YES,
        R.string.dmc_dark_mode_name,
        R.string.dmc_dark_mode_description,
        isSelected
    ) {
        override fun copyModel(isSelected: Boolean): UiMode {
            return this.copy(isSelected = isSelected)
        }
    }

    data class FollowSystemSetting(override val isSelected: Boolean) : UiMode(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        R.string.dmc_follow_system_name,
        R.string.dmc_follow_system_description,
        isSelected
    ) {
        override fun copyModel(isSelected: Boolean): UiMode {
            return this.copy(isSelected = isSelected)
        }
    }

    companion object {
        fun getOptionList(screenMode: Int): List<UiMode> {
            return listOf(
                Light(isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_NO),
                Dark(isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_YES),
                FollowSystemSetting(isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            )
        }
    }
}