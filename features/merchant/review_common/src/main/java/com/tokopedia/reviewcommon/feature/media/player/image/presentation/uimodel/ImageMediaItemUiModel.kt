package com.tokopedia.reviewcommon.feature.media.player.image.presentation.uimodel

import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageMediaItemUiModel(
    override val id: String,
    override val uri: String,
    override val mediaNumber: Int,
    override val showSeeMore: Boolean,
    override val totalMediaCount: Int,
    override val feedbackId: String
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = id == other?.id
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = this == other
}
