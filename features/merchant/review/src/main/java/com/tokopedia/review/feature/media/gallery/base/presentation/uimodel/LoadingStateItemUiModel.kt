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
    override fun areItemTheSame(other: MediaItemUiModel?): Boolean {
        // here we don't check whether the other item is LoadingStateItemUiModel or not because
        // if the other item is ImageMediaItemUiModel or VideoMediaItemUiModel, we want to tell the
        // adapter that that is the same item and we want to just update the item
        return mediaNumber == other?.mediaNumber
    }
    override fun areContentsTheSame(other: MediaItemUiModel?): Boolean {
        // here we only check whether the other item is LoadingStateItemUiModel or not so that
        // when the item on this index is replaced by ImageMediaItemUiModel or VideoMediaItemUiModel,
        // it will get rebind
        return other is LoadingStateItemUiModel
    }
    override fun getAttachmentID(): String = ""
}
