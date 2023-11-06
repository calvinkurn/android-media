package com.ilhamsuaib.darkmodeconfig.model

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
//noinspection MissingResourceImportAlias
import com.ilhamsuaib.darkmodeconfig.R

/**
 * Created by @ilhamsuaib on 05/11/23.
 */

data class ThemeOptionUiModel(
    val screenMode: Int,
    @StringRes val nameResId: Int,
    @StringRes val descriptionResId: Int,
    val isSelected: Boolean
) {

    companion object {
        fun getOptionList(screenMode: Int): List<ThemeOptionUiModel> {
            return listOf(
                ThemeOptionUiModel(
                    screenMode = AppCompatDelegate.MODE_NIGHT_NO,
                    nameResId = R.string.dmc_light_mode_name,
                    descriptionResId = R.string.dmc_light_mode_description,
                    isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_NO
                ),
                ThemeOptionUiModel(
                    screenMode = AppCompatDelegate.MODE_NIGHT_YES,
                    nameResId = R.string.dmc_dark_mode_name,
                    descriptionResId = R.string.dmc_dark_mode_description,
                    isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_YES
                ),
                ThemeOptionUiModel(
                    screenMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                    nameResId = R.string.dmc_follow_system_name,
                    descriptionResId = R.string.dmc_follow_system_description,
                    isSelected = screenMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            )
        }
    }
}