package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaImageThumbnailUiModel(
    val uiState: ReviewMediaImageThumbnailUiState
) : ReviewMediaThumbnailVisitable {
    override fun areItemsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean {
        return newItem is ReviewMediaImageThumbnailUiModel && uiState.thumbnailUrl == newItem.uiState.thumbnailUrl
    }

    override fun areContentsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean {
        return this == newItem
    }

    override fun getChangePayload(newItem: ReviewMediaThumbnailVisitable?): List<String> {
        return emptyList()
    }

    override fun getReviewID(): String {
        return uiState.reviewID
    }

    override fun type(typeFactory: ReviewMediaThumbnailTypeFactory): Int {
        return typeFactory.type(this)
    }
}
