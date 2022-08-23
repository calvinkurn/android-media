package com.tokopedia.review.feature.media.gallery.base.presentation.uimodel

import com.tokopedia.kotlin.extensions.view.ZERO

data class LoadingStateItemUiModel(
    override val id: String = "",
    override val uri: String = "",
    override val mediaNumber: Int,
    override val showSeeMore: Boolean = false,
    override val totalMediaCount: Int = Int.ZERO,
    override val feedbackId: String = ""
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = other is LoadingStateItemUiModel && mediaNumber == other.mediaNumber
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = other is LoadingStateItemUiModel && mediaNumber == other.mediaNumber
    override fun getAttachmentID(): String = ""
}