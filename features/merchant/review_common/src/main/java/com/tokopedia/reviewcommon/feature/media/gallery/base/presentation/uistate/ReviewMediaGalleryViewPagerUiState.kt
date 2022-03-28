package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaGalleryViewPagerUiState(
    val currentPagerPosition: Int = Int.ZERO, val enableUserInput: Boolean = true
) : Parcelable
