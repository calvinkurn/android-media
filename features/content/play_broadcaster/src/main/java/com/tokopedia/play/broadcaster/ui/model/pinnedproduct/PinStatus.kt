package com.tokopedia.play.broadcaster.ui.model.pinnedproduct

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by astidhiyaa on 13/07/22
 */

sealed interface PinUiModel : Parcelable

@Parcelize
data class PinProductUiModel(
    val pinStatus: PinStatus,
    val canPin: Boolean,
    val isLoading: Boolean = false,
): PinUiModel {
    companion object {
        val Empty = PinProductUiModel(pinStatus = PinStatus.Unpin, canPin = false, isLoading = false)
    }
}

enum class PinStatus(val status: Boolean){
    Pinned(status = true),
    Unpin(status = false);

    companion object {
        fun getPinStatus(status: Boolean): PinStatus = if (status) Pinned else Unpin
    }
}