package com.tokopedia.review.feature.media.gallery.base.presentation.uistate

import android.os.Parcelable
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdapterUiState(
    val mediaItemUiModels: List<MediaItemUiModel>
): Parcelable
