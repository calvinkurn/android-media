package com.tokopedia.play.broadcaster.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
@Parcelize
data class FaceFilterUiModel(
    val name: String,
    val minValue: Double,
    val maxValue: Double,
    val defaultValue: Double,
    val value: Double,
    val iconUrl: String,
    val assetLink: String,
    val isSelected: Boolean,
    val assetStatus: AssetStatus,
) : Parcelable {

    val isChecked: Boolean
        get() = value != defaultValue

    enum class AssetStatus {
        Available,
        Downloading,
        NotDownloaded,
        Unknown
    }
}
