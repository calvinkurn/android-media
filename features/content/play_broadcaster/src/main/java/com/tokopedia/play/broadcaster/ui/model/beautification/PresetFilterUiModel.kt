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

    companion object {
        private const val OPTION_NONE = "none"
    }
}
