package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel

import android.os.Parcelable

interface MediaItemUiModel : Parcelable {
    val id: String
    val uri: String
    val mediaNumber: Int
    val showSeeMore: Boolean
    val totalMediaCount: Int
    val feedbackId: String
    fun areItemTheSame(other: MediaItemUiModel?): Boolean
    fun areContentsTheSame(other: MediaItemUiModel?): Boolean
}