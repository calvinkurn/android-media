package com.tokopedia.play.broadcaster.ui.model.pinnedproduct

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by astidhiyaa on 13/07/22
 */

sealed interface PinUiModel : Parcelable

@Parcelize
data class PinProductUiModel(
    val isPinned: Boolean,
    val canPin: Boolean,
    val isLoading: Boolean = false,
): PinUiModel {

    companion object {
        val Empty = PinProductUiModel(
            isPinned = false,
            canPin = false,
            isLoading = false,
        )
    }
}