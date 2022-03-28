package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uistate

import android.os.Parcelable
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaGalleryAdapterUiState(
    val mediaItemUiModels: List<MediaItemUiModel>
): Parcelable
