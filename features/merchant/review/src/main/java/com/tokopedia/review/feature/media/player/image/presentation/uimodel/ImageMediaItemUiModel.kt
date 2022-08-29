package com.tokopedia.review.feature.media.player.image.presentation.uimodel

import android.net.Uri
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel

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

    override fun getAttachmentID(): String {
        return if (id.startsWith("http")) "" else id
    }

    fun getFileName(): String {
        return Uri.parse(uri).lastPathSegment.orEmpty()
    }
}
