package com.tokopedia.reviewcommon.feature.media.player.video.presentation.model

import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoMediaItemUiModel(
    override val uri: String = "",
    override val mediaNumber: Int,
    override val showSeeMore: Boolean,
    override val totalMediaCount: Int
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = uri == other?.uri
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = this == other
}
