package com.tokopedia.review.feature.media.gallery.base.presentation.uimodel

interface MediaItemUiModel {
    val id: String
    val uri: String
    val mediaNumber: Int
    val showSeeMore: Boolean
    val totalMediaCount: Int
    val feedbackId: String
    fun areItemTheSame(other: MediaItemUiModel?): Boolean
    fun areContentsTheSame(other: MediaItemUiModel?): Boolean
    fun getAttachmentID(): String
}