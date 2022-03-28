package com.tokopedia.reviewcommon.feature.media.player.image.presentation.uistate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ReviewImagePlayerUiState: Parcelable {
    val imageUri: String
    @Parcelize data class Showing(override val imageUri: String = "") : ReviewImagePlayerUiState
}