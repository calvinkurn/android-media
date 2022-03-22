package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState

data class ReviewMediaVideoThumbnailUiModel(
    val uiState: ReviewMediaVideoThumbnailUiState
) : ReviewMediaThumbnailVisitable {
    override fun areItemsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean {
        return newItem is ReviewMediaVideoThumbnailUiModel && uiState.uri == newItem.uiState.uri
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