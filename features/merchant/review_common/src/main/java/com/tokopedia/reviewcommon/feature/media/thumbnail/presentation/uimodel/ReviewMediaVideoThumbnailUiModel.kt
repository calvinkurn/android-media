package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewMediaVideoThumbnailUiModel(
    val uiState: ReviewMediaVideoThumbnailUiState
) : ReviewMediaThumbnailVisitable {
    override fun areItemsTheSame(newItem: ReviewMediaThumbnailVisitable?): Boolean {
        return newItem is ReviewMediaVideoThumbnailUiModel && uiState.url == newItem.uiState.url
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

    override fun getAttachmentID(): String {
        return uiState.attachmentID.takeIf {
            (it.isNotBlank() && it.toLongOrNull() == null) || it.toLongOrZero().isMoreThanZero()
        } ?: uiState.url
    }

    override fun getTotalMediaCount(): Long {
        return if (uiState is ReviewMediaVideoThumbnailUiState.ShowingSeeMore) {
            uiState.totalMediaCount.toLong()
        } else -1
    }

    override fun isShowingSeeMore(): Boolean {
        return uiState is ReviewMediaVideoThumbnailUiState.ShowingSeeMore
    }

    override fun type(typeFactory: ReviewMediaThumbnailTypeFactory): Int {
        return typeFactory.type(this)
    }
}