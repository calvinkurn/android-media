package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaThumbnailUiModel(
    val mediaThumbnails: List<ReviewMediaThumbnailVisitable> = emptyList()
) : Parcelable