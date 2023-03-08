package com.tokopedia.play.broadcaster.ui.model.beautification

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
@Parcelize
data class FaceFilterUiModel(
    val id: String,
    val name: String,
    val minValue: Double,
    val maxValue: Double,
    val defaultValue: Double,
    val value: Double,
    val iconUrl: String,
    val assetLink: String,
    val isSelected: Boolean,
    val assetStatus: BeautificationAssetStatus,
) : Parcelable {

    val isChecked: Boolean
        get() = value != 0.0

    val isRemoveEffect: Boolean
        get() = id == OPTION_NONE

    companion object {
        private const val OPTION_NONE = "none"
    }
}
