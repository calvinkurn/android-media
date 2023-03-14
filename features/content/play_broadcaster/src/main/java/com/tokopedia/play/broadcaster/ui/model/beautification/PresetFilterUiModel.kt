package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on March 08, 2023
 */
@Parcelize
data class PresetFilterUiModel(
    val id: String,
    val name: String,
    val active: Boolean,
    val minValue: Double,
    val maxValue: Double,
    val defaultValue: Double,
    val value: Double,
    val iconUrl: String,
    val assetLink: String,
    val assetStatus: BeautificationAssetStatus,
) : Parcelable {

    val isRemoveEffect: Boolean
        get() = id == OPTION_NONE

    val minValueForSlider: Int
        get() = (minValue * PERCENTAGE_MULTIPLIER).toInt()

    val maxValueForSlider: Int
        get() = (maxValue * PERCENTAGE_MULTIPLIER).toInt()

    val defaultValueForSlider: Int
        get() = (defaultValue * PERCENTAGE_MULTIPLIER).toInt()

    val valueForSlider: Int
        get() = (value * PERCENTAGE_MULTIPLIER).toInt()

    fun copyWithNewValue(newValueFromSlider: Int): PresetFilterUiModel {
        return copy(
            value = newValueFromSlider / PERCENTAGE_MULTIPLIER.toDouble()
        )
    }

    companion object {
        private const val OPTION_NONE = "none"

        private const val PERCENTAGE_MULTIPLIER = 100
    }
}
