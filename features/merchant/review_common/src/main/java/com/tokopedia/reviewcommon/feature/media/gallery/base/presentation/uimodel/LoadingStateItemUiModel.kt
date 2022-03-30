package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel

import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoadingStateItemUiModel(
    override val uri: String = "",
    override val mediaNumber: Int,
    override val showSeeMore: Boolean = false,
    override val totalMediaCount: Int = Int.ZERO
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = other is LoadingStateItemUiModel && mediaNumber == other.mediaNumber
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = other is LoadingStateItemUiModel && mediaNumber == other.mediaNumber
}