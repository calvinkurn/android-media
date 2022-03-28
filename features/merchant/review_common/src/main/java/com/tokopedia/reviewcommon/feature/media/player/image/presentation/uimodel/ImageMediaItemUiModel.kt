package com.tokopedia.reviewcommon.feature.media.player.image.presentation.uimodel

import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageMediaItemUiModel(
    override val uri: String, override val mediaNumber: Int
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = other is ImageMediaItemUiModel && uri == other.uri
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = this == other
}
