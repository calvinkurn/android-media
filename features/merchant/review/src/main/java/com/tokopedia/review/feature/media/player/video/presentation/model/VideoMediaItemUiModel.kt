package com.tokopedia.review.feature.media.player.video.presentation.model

import android.net.Uri
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel

data class VideoMediaItemUiModel(
    override val id: String,
    override val uri: String = "",
    override val mediaNumber: Int,
    override val showSeeMore: Boolean,
    override val totalMediaCount: Int,
    override val feedbackId: String
) : MediaItemUiModel {
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean = id == other?.id
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean = this == other

    override fun getAttachmentID(): String {
        return if (id.startsWith("http")) "" else id
    }

    fun getVideoID(): String {
        return Uri.parse(uri).getQueryParameter("id").orEmpty()
    }
}
