package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel

import kotlinx.parcelize.Parcelize

@Parcelize
data class LoadingStateItemUiModel(
    override val uri: String = "",
    override val mediaNumber: Int
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = other is LoadingStateItemUiModel && mediaNumber == other.mediaNumber
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = other is LoadingStateItemUiModel && mediaNumber == other.mediaNumber
}