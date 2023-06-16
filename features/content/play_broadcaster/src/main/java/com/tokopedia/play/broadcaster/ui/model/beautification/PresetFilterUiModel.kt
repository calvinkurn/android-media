package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import com.tokopedia.content.common.util.toPercent
import com.tokopedia.content.common.util.toUnitInterval
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
    val isSelected: Boolean,
) : Parcelable {

    val isRemoveEffect: Boolean
        get() = id == OPTION_NONE

    val minValueForSlider: Int
        get() = minValue.toPercent()

    val maxValueForSlider: Int
        get() = maxValue.toPercent()

    val defaultValueForSlider: Int
        get() = defaultValue.toPercent()

    val valueForSlider: Int
        get() = value.toPercent()

    fun copyWithNewValue(newValueFromSlider: Int): PresetFilterUiModel {
        return copy(
            value = newValueFromSlider.toUnitInterval()
        )
    }

    companion object {
        private const val OPTION_NONE = "none"

        fun isNone(id: String) = id == OPTION_NONE
    }
}
