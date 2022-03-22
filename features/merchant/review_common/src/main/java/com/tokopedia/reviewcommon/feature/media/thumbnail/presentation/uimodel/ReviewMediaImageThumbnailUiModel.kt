package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaImageThumbnailUiModel(
    val uiState: ReviewMediaImageThumbnailUiState
) : ReviewMediaThumbnailVisitable {
    override fun areItemsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean {
        return newItem is ReviewMediaImageThumbnailUiModel && uiState.uri == newItem.uiState.uri
    }

    override fun areContentsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean {
        return this == newItem
    }

    override fun getChangePayload(newItem: ReviewMediaThumbnailVisitable?): List<String> {
        return emptyList()
    }

    override fun type(typeFactory: ReviewMediaThumbnailTypeFactory): Int {
        return typeFactory.type(this)
    }
}
